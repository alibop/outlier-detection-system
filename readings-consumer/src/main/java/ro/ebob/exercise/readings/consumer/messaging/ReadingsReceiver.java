package ro.ebob.exercise.readings.consumer.messaging;

import ro.ebob.exercise.readings.consumer.domain.ReadingsMessage;

public interface ReadingsReceiver {

  void receive(ReadingsMessage data);
}
