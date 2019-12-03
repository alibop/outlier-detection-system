package ro.ebob.exercise.readings.web;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ro.ebob.exercise.readings.data.domain.ReadingsEntity;
import ro.ebob.exercise.readings.data.repository.ReadingsRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude= {
    DataSourceAutoConfiguration.class, 
    DataSourceTransactionManagerAutoConfiguration.class, 
    HibernateJpaAutoConfiguration.class
})
class PublishersControllerTest {

  public final static String SAMPLE_DATA = "[" +
      "{\"id\":8,\"publisher\":\"pubTest\",\"time\":\"2019-12-03 13:8:03.040\", \"median\":8.0}," +
      "{\"id\":7,\"publisher\":\"pubTest\",\"time\":\"2019-12-03 13:43:27.040\",\"median\":3.0}," +
      "{\"id\":6,\"publisher\":\"pubTest\",\"time\":\"2019-12-03 14:17:16.985\",\"median\":1.0}," + 
      "{\"id\":5,\"publisher\":\"pubTest\",\"time\":\"2019-12-03 14:17:10.194\",\"median\":2.0}," +
      "{\"id\":4,\"publisher\":\"pubTest\",\"time\":\"2019-12-03 14:12:42.854\",\"median\":8.0}," +
      "{\"id\":3,\"publisher\":\"pubTest\",\"time\":\"2019-12-03 14:12:00.192\",\"median\":20.0}," + //outlier:true 
      "{\"id\":2,\"publisher\":\"pubTest\",\"time\":\"2019-12-03 14:11:37.806\",\"median\":5.0}," +
      "{\"id\":1,\"publisher\":\"pubTest\",\"time\":\"2019-12-03 13:43:27.040\",\"median\":2.0}]";
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private ReadingsRepository readingsRepository;
  

  @Test
  void test_given_publisher_for_outliers() throws Exception {
    //given the list above
    List<ReadingsEntity> givenDbResults = new ObjectMapper().readValue(SAMPLE_DATA, new TypeReference<List<ReadingsEntity>>(){}); 
  
    when(readingsRepository.findAll(ArgumentMatchers.<Specification<ReadingsEntity>>any(), any(PageRequest.class))).thenReturn(new PageImpl<ReadingsEntity>(givenDbResults));
    
    //outlier is 20
    this.mockMvc.perform(
        get("/publishers/{publisher}/outliers", "pubTest").accept(MediaType.APPLICATION_JSON))
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.length()", is(8)))
    .andExpect(jsonPath("[?(@.id=='3' && @.outlier == true)]").exists());
  }

}
