package ru.magnit.magreportbackend.mapper.securityfilter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.magnit.magreportbackend.domain.filterinstance.FilterInstanceField;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRoleTuple;
import ru.magnit.magreportbackend.domain.securityfilter.SecurityFilterRoleTupleValue;

import java.time.LocalDateTime;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class SecurityFilterRoleTupleValueResponseMapperTest {


 @InjectMocks
 private SecurityFilterRoleTupleValueResponseMapper mapper;

 private final static Long ID = 1L;
 private final static String VALUE = "Value";
 private final static LocalDateTime CREATED = LocalDateTime.now();
 private final static LocalDateTime MODIFIED = LocalDateTime.now().plusDays(1);


 @Test
 void from() {

  var response = mapper.from(getSecurityFilterRoleTupleValue());

  Assertions.assertEquals(ID, response.getFieldId());
  Assertions.assertEquals(VALUE, response.getValue());

 }

 @Test
 void fromList() {

  var responses = mapper.from(Collections.singletonList(getSecurityFilterRoleTupleValue()));
  Assertions.assertEquals(1, responses.size());
  var response = responses.get(0);

  Assertions.assertEquals(ID, response.getFieldId());
  Assertions.assertEquals(VALUE, response.getValue());

 }

 private SecurityFilterRoleTupleValue getSecurityFilterRoleTupleValue() {
  return new SecurityFilterRoleTupleValue()
          .setId(ID)
          .setValue(VALUE)
          .setTuple(new SecurityFilterRoleTuple())
          .setField(new FilterInstanceField().setId(ID))
          .setCreatedDateTime(CREATED)
          .setModifiedDateTime(MODIFIED);
 }

}
