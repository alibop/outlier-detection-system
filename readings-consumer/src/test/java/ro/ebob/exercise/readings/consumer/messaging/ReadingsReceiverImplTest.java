package ro.ebob.exercise.readings.consumer.messaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.ebob.exercise.readings.ReadingsConsumerApplication;
import ro.ebob.exercise.readings.consumer.domain.ReadingsMessage;
import ro.ebob.exercise.readings.data.domain.ReadingsEntity;
import ro.ebob.exercise.readings.data.service.ReadingsDataService;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { ReadingsConsumerApplication.class, TestChannelBinderConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableAutoConfiguration(exclude= {
    DataSourceAutoConfiguration.class, 
    DataSourceTransactionManagerAutoConfiguration.class, 
    HibernateJpaAutoConfiguration.class
})
class ReadingsReceiverImplTest {

  public static final String PAYLOAD = "{\"publisher\":\"pubTest\",\"time\":\"2019-12-03 18:03.040\",\"readings\":[7,8,9]}";
  
  @Autowired 
  InputDestination source;
  
  @Autowired 
  OutputDestination target;
  
  @SpyBean
  ReadingsReceiver subscriber;
  
  @MockBean
  ReadingsDataService readingsDataService;

  @Test
  public void test_receive_message_n_save_to_db() throws JsonMappingException, JsonProcessingException {
    ReadingsMessage expected = new ObjectMapper().readValue(PAYLOAD, ReadingsMessage.class);  
    
    //when a message is published
    Message<String> message = new GenericMessage<>(PAYLOAD);
    source.send(message);
    
   
    
    //subscriber receives it
    ArgumentCaptor<ReadingsMessage> readingsArgument = ArgumentCaptor.forClass(ReadingsMessage.class);    
    verify(this.subscriber, times(1)).receive(readingsArgument.capture());
    assertEquals(expected.getPublisher(), readingsArgument.getValue().getPublisher());
    assertEquals(expected.getTime(), readingsArgument.getValue().getTime());
    
    //and saves it to the database
    ArgumentCaptor<ReadingsEntity> entityArgument = ArgumentCaptor.forClass(ReadingsEntity.class);
    verify(this.readingsDataService, times(1)).save(entityArgument.capture());
    assertEquals(expected.getPublisher(), entityArgument.getValue().getPublisher());
    assertEquals(expected.getTime(), entityArgument.getValue().getTime());
    assertEquals(8.0, entityArgument.getValue().getMedian());
  }
}
