package be.kdg.ip3.archportal.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;

@Slf4j
public class QuietDlqRecoverer implements MessageRecoverer {
    private final RabbitTemplate rabbitTemplate;

    public QuietDlqRecoverer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void recover(Message message, Throwable cause) {
        Throwable rootCause = getRootCause(cause);

        String originalQueue = message.getMessageProperties().getConsumerQueue();
        String dlqName = originalQueue + ".dlq";

        log.warn(
                "Retries exhausted for message from queue {}. Reason: {}",
                originalQueue,
                rootCause.getMessage()
        );

        rabbitTemplate.send(dlqName, message);
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable result = throwable;
        while (result.getCause() != null && result.getCause() != result) {
            result = result.getCause();
        }
        return result;
    }
}