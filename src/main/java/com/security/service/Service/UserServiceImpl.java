package com.security.service.Service;

import com.security.service.DTO.UserDto;
import com.security.service.Entity.Location;
import com.security.service.Entity.SOS;
import com.security.service.Entity.User;
import com.security.service.Exceptions.CannotBeNullException;
import com.security.service.Exceptions.IncorrectPasswordException;
import com.security.service.Exceptions.UserNotFoundException;
import com.security.service.Jwt.JwtTokenGenerator;
import com.security.service.Model.LoginRequest;
import com.security.service.Model.LoginResponse;
import com.security.service.Repository.UserRepo;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    HtmlCreator creator;
    @Autowired
    MailService mailService;
    @Autowired
    UserRepo userRepo;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    JwtTokenGenerator jwtUtil;

    @Override
    public UserDto add(User user) throws UserNotFoundException {
        if(userRepo.existsByEmail(user.getEmail())) throw new UserNotFoundException("User Exists");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setCreateTime(Instant.now());
        user.setLive(false);
        if(user.getLocation()==null) {
            user.setLocation(new Location());
        }
        return UserDto.entityToDto(userRepo.save(user));
    }

    @Override
    public List<User> get() throws UserNotFoundException{
        return userRepo.findAll();
    }

    @Override
    public LoginResponse authenticate(LoginRequest request) throws UserNotFoundException, IncorrectPasswordException, MessagingException {
        User user = userRepo.findByEmail(request.getUsername().toLowerCase())
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        if (!bCryptPasswordEncoder.matches(request.getPassword(),user.getPassword()))
            throw new IncorrectPasswordException("Incorrect Password");

        LoginResponse response = new LoginResponse();

        String token = jwtUtil.generateToken(user);
        response.setToken(token);
        response.setRefreshToken(token);
        response.setUserDto(UserDto.entityToDto(user));

        mailService.sendHtmlEmail(request.getUsername(), "Account Was Logged In",creator.getHtml(user.getFirstName()));
        
        return response;
    }

    @Override
    public UserDto update(UserDto dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(()-> new UserNotFoundException("User Does Not Exits"));
        Boolean update = false;
        if (Objects.nonNull(dto.getFirstName())) user.setFirstName(dto.getFirstName());update = true;
        if (Objects.nonNull(dto.getLastName())) user.setLastName(dto.getLastName());update = true;
        if (Objects.nonNull(dto.getPhoneNumber())) user.setPhoneNumber(dto.getPhoneNumber());update = true;
        if (Objects.nonNull(dto.getAllowedUsers())) user.setAllowedUsers(dto.getAllowedUsers());update = true;
        if (Objects.nonNull(dto.isLive())) user.setLive(dto.isLive());update = true;
        if (update) user.setUpdateTime(Instant.now());
        System.out.println("User Update: "+user.getPhoneNumber());

        return UserDto.entityToDto(userRepo.save(user));
    }

    @Override
    public User view(String email) throws UserNotFoundException{
        return userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
    }

    @Override
    public void delete(String email) throws UserNotFoundException {
        User user = userRepo.findByEmail(email)
                        .orElseThrow(()->new UserNotFoundException("User Not Found"));
        userRepo.delete(user);
    }

    @Override
    public Set<String> addWatchLive(UserDto userDto, String email) throws UserNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        //checking if the user I want to add has an account with us or not.
        userRepo.findByEmail(userDto.getEmail())
                .orElseThrow(()->new UserNotFoundException("Cannot Added User. Ask the user to create an account"));

        Set<String> addUser = user.getAllowedUsers();
        addUser.add(userDto.getEmail());
        userRepo.save(user);
        return addUser;
    }
    @Override
    public Set<String> addLiveListeners(UserDto dto, String email) throws UserNotFoundException, CannotBeNullException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        if (dto.getAllowedUsers()!=null && !dto.getAllowedUsers().isEmpty()){
            if (user.getAllowedUsers()==null) user.setAllowedUsers(new HashSet<>());
            user.getAllowedUsers().addAll(dto.getAllowedUsers());
            userRepo.save(user);
        }
        else throw new CannotBeNullException("You cannot add null values");
        return user.getAllowedUsers();
    }

    @Override
    public List<String> searchEmail(String search) {
        if (search!=null && !search.isEmpty()) search = search.toLowerCase();
        return userRepo.searchByEmail(search);
    }

    @Override
    public SOS addSos(UserDto dto, String email) throws UserNotFoundException,CannotBeNullException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        if (dto.getSosContacts()!=null && !dto.getSosContacts().isEmpty()){
            if (user.getSosContact()==null){
                SOS createSos = new SOS();
                if (dto.getSosMessage()==null || dto.getSosMessage()=="") createSos.setSosMessage("Please Help Me! I am in a danger. Here is my Live Location.");
                createSos.setSosContacts(dto.getSosContacts());
                user.setSosContact(createSos);
            }
            else {
                if (dto.getSosMessage()!=null || dto.getSosMessage()=="") user.getSosContact().setSosMessage(dto.getSosMessage());
                user.getSosContact().getSosContacts().addAll(dto.getSosContacts());
            }
        }
        else throw new CannotBeNullException("You cannot add empty value");
        return userRepo.save(user).getSosContact();
    }

    //loop through the sosConacts and send them the email of current location.
    @Override
    public SOS initateSOS(String email, String sosLocation) throws UserNotFoundException, CannotBeNullException{
        User user = userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        if (user.getSosContact()==null || user.getSosContact().getSosContacts().isEmpty()) throw new CannotBeNullException("Please add sos contacts");
        user.setLive(true);
        try {
            //sending to the
            mailService.sendHtmlEmail("asharshahab@gmail.com", "It is an emergency!",
                    creator.sendSOS(user.getFirstName(), "Ashar", user.getLocation().getLongitude(), user.getLocation().getLatitude(),sosLocation));
            for (String sendingEmailTo : user.getSosContact().getSosContacts()) {
                mailService.sendHtmlEmail(sendingEmailTo, "It is an emergency!",
                        creator.sendSOS(user.getFirstName(), sendingEmailTo, user.getLocation().getLongitude(), user.getLocation().getLatitude(),sosLocation));
            }
            user.getSosContact().setSosInitiatedAt(Instant.now());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return userRepo.save(user).getSosContact();
    }

    @Cacheable(value = "viewContactsCache")
    @Override
    public List<String> viewContacts(String email,String type) throws UserNotFoundException{
        User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User Not Found"));
        List<String> contacts = new ArrayList<>();
        if (type==null) {
            if (user.getSosContact() != null) {
                contacts.addAll(user.getSosContact().getSosContacts());
            }
            if (user.getAllowedUsers() != null || !user.getAllowedUsers().isEmpty()) {
                contacts.addAll(user.getAllowedUsers());
            }
            return contacts.stream()
                    .distinct()
                    .collect(Collectors.toList());
        }
        else if (type.equalsIgnoreCase("sos")){
            if (user.getSosContact() != null) {
                contacts.addAll(user.getSosContact().getSosContacts());
            }
            return contacts;
        }
        else if (type.equalsIgnoreCase("live")){
            if (user.getAllowedUsers() != null || !user.getAllowedUsers().isEmpty()) {
                contacts.addAll(user.getAllowedUsers());
            }
            return contacts;
        }
        return null;
    }
    //not using this function anymore
    @Override
    public List<String> searchList(String email,String query, String type) throws UserNotFoundException{
        User user = userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        //logic for search query
        List<String> searchContact = new ArrayList<>();
        if (type==null || type.isEmpty()){
            if (user.getSosContact()!=null){
                searchContact.addAll(user.getSosContact().getSosContacts());
            }
            if (user.getAllowedUsers()!=null){
                searchContact.addAll(user.getAllowedUsers());
            }
            //returning the best match
            return searchContact.stream()
                    .filter(contact -> contact.contains(query) || hasPartialMatch(contact,query))
                    .sorted(Comparator.comparingInt(contact -> matchScore(contact,query)))
                    .collect(Collectors.toList());
        }
        if (type.equalsIgnoreCase("live")){
            searchContact.addAll(user.getAllowedUsers());
            return searchContact.stream()
                    .filter(contact -> contact.contains(query) || hasPartialMatch(contact,query))
                    .sorted(Comparator.comparingInt(contact -> matchScore(contact,query)))
                    .collect(Collectors.toList());
        }
        if (type.equalsIgnoreCase("sos")){
            searchContact.addAll(user.getSosContact().getSosContacts());
            return searchContact.stream()
                    .filter(contact -> contact.contains(query) || hasPartialMatch(contact,query))
                    .sorted(Comparator.comparingInt(contact -> matchScore(contact,query)))
                    .collect(Collectors.toList());
        }
        return null;
    }
    //search engine logic
    //helper function to get the match as boolean
    private boolean hasPartialMatch(String str, String search){
        int searchIndex = 0;
        for (char c:str.toCharArray()){
            if (c==search.charAt(searchIndex)){
                searchIndex++;
                if(searchIndex==search.length()) return true;
            }
        }
        return false;
    }
    //helper function to generate the match score 0->best match,1->good match,2->partial match,3->no match
    //will be using it for comparing the results and sorting them accordingly
    private int matchScore(String str, String search){
        if (str.startsWith(search)) return 0;
        else if (str.contains(search)) return 1;
        else if (hasPartialMatch(str,str)) return 2;
        else return 3;
    }
}
