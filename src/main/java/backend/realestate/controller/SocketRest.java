package backend.realestate.controller;

import backend.realestate.model.Message;
import backend.realestate.model.User;
import backend.realestate.repository.MessageRepository;
import backend.realestate.repository.UserRepository;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/api/socket")
@CrossOrigin
public class SocketRest {
    @Autowired
    private SimpMessagingTemplate simpleMessage;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity useSimpleRest(@RequestBody Map<String, String> message) {
        if (message.containsKey("message")) {
            if (message.containsKey("toId") && message.get("toId") != null && !message.get("toId").equals("")) {
                User toUser = userRepository.findByUsername(message.get("toId")).orElseThrow(() -> new RuntimeException("cannot find this user"));
                User fromUser = userRepository.findByUsername(message.get("fromId")).orElseThrow(() -> new RuntimeException("cannot find this user"));
                Message messageContact = new Message(fromUser, toUser, message.get("message"), false);
                messageRepository.save(messageContact);
                this.simpleMessage.convertAndSend("/socket-publisher/" + message.get("toId"), message);
                this.simpleMessage.convertAndSend("/socket-publisher/" + message.get("fromId"), message);
            } else {
                this.simpleMessage.convertAndSend("/socket-publisher", message);
            }
            return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/getContact")
    public ResponseEntity<List<Message>> getContact(@RequestBody Map<String, String> message) {
        User toUser = userRepository.findByUsername(message.get("toId")).orElseThrow(() -> new RuntimeException("Cannot find this user"));
        User fromUser = userRepository.findByUsername(message.get("fromId")).orElseThrow(() -> new RuntimeException("Cannot find this user"));
        List<Message> messageList1 = messageRepository.getAllByToUserAndFromUserOrderByCreatedDate(toUser, fromUser);
        List<Message> messageLists2 = messageRepository.getAllByToUserAndFromUserOrderByCreatedDate(fromUser, toUser);
        List<Message> newList = Stream.concat(messageList1.stream(), messageLists2.stream())
                .collect(Collectors.toList());
        Collections.sort(newList, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return o1.getCreatedDate().compareTo(o2.getCreatedDate());
            }
        });
        return new ResponseEntity<List<Message>>(newList, HttpStatus.OK);
    }

    @MessageMapping("/send/message")
    public Map<String, String> useSocketCommunication(String message) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> messageConverted = null;
        try {
            messageConverted = mapper.readValue(message, Map.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (messageConverted != null) {
            if (messageConverted.containsKey("toId") && messageConverted.get("toId").isEmpty()) {
                this.simpleMessage.convertAndSend("/socket-publisher/" + messageConverted.get("toId"), messageConverted);
                this.simpleMessage.convertAndSend("/socket-publisher/" + messageConverted.get("fromId"), message);
            } else {
                this.simpleMessage.convertAndSend("/socket-publisher", messageConverted);
            }
        }
        return messageConverted;
    }
}
