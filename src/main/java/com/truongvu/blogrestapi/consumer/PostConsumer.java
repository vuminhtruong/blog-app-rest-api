package com.truongvu.blogrestapi.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.truongvu.blogrestapi.config.RabbitMQConfig;
import com.truongvu.blogrestapi.dto.PostDTO;
import com.truongvu.blogrestapi.entity.Category;
import com.truongvu.blogrestapi.entity.Post;
import com.truongvu.blogrestapi.exception.BlogAPIException;
import com.truongvu.blogrestapi.exception.ResourceNotFoundException;
import com.truongvu.blogrestapi.repository.CategoryRepository;
import com.truongvu.blogrestapi.repository.PostRepository;
import com.truongvu.blogrestapi.service.PostService;
import com.truongvu.blogrestapi.utils.PostMapping;
import com.truongvu.blogrestapi.validate.handler.ValidationHandler;
import com.truongvu.blogrestapi.validate.post.ValidatePostCategory;
import com.truongvu.blogrestapi.validate.post.ValidatePostImage;
import com.truongvu.blogrestapi.validate.post.ValidatePostLength;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.truongvu.blogrestapi.utils.PostMapping.mapToEntity;

@Service
@RequiredArgsConstructor
public class PostConsumer {
    private final ObjectMapper objectMapper;
    private final PostService postService;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ValidationHandler validationHandler = ValidationHandler.link(new ValidatePostLength(), new ValidatePostCategory(), new ValidatePostImage());


    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    @Transactional
    public void receivedMessage(String postProducer, MessageProperties messageProperties) {
        PostDTO postDTO;

        try {
            postDTO = objectMapper.readValue(postProducer, PostDTO.class);
        } catch (JsonProcessingException exception) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Post is not correct!");
        }

        Category category;

        try {
            long category_id = postDTO.getCategoryId();
            category = categoryRepository.findById(category_id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", postDTO.getCategoryId()));
        } catch (Exception exception) {
            category = null;
        }
        Post post = mapToEntity(postDTO);
        post.setCategory(category);
        if (validationHandler.check(post) != null) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, validationHandler.check(post));
        }
        try {
            postRepository.insertNewPost(post);
        } catch (Exception exception) {
            rabbitTemplate.convertAndSend("error-exchange", "error-routingkey", exception.getMessage(), message -> {
                message.getMessageProperties().setCorrelationId(messageProperties.getCorrelationId());
                return message;
            });
        }
    }

    @RabbitListener(queues = RabbitMQConfig.RPC_QUEUE1)
    public void getAllPost(Message message) throws IOException {
        List<PostDTO> postDTOList = objectMapper.readValue(message.getBody(), new TypeReference<List<PostDTO>>() {
        });

        postDTOList.addAll(postService.getAllPostsWithoutPageSize());

        Message response = MessageBuilder
                .withBody(objectMapper.writeValueAsString(postDTOList).getBytes())
                .setCorrelationId(message.getMessageProperties().getCorrelationId())
                .build();

        CorrelationData correlationData = new CorrelationData(message.getMessageProperties().getCorrelationId());
        rabbitTemplate.send(RabbitMQConfig.RPC_EXCHANGE, RabbitMQConfig.RPC_QUEUE2, response, correlationData);
    }
}
