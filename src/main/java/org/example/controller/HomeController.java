package org.example.controller;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

@Controller
public class HomeController {

    @Autowired
    HazelcastInstance hazelcastInstance;
    private IMap<String, Map<String, Object>> sessionsAttributes;

    @GetMapping("/")
    public String home(HttpSession session) throws NoSuchAlgorithmException {
        session.setAttribute("web_login", "web_login");
        //measuring elapsed time using Spring StopWatch
        StopWatch watch = new StopWatch();
        watch.start();
        SecureRandom random =  SecureRandom.getInstance("SHA1PRNG");
       int num = random.nextInt();
        watch.stop();
        System.out.println("num1 = {" + num + "} and SecureRandom random nanos: "
                + watch.getTotalTimeNanos() );

        StopWatch watch3 = new StopWatch();
        watch3.start();
        SecureRandom random3 =  new SecureRandom();
        int num3 = random3.nextInt();
        watch3.stop();
        System.out.println("num2 = {" + num3 + "} and SecureRandom random nanos: "
                + watch3.getTotalTimeNanos());


        StopWatch watch4 = new StopWatch();
        watch4.start();
        SecureRandom random4 = SecureRandom.getInstance("DRBG");
        int num4 = random4.nextInt();
        watch4.stop();
        System.out.println("num3 = {" + num4 + "} and SecureRandom random nanos: "
                + watch4.getTotalTimeNanos());

        StopWatch watch2 = new StopWatch();
        watch2.start();
        Random random2 =   new Random(System.currentTimeMillis());
        int num2 = random2.nextInt();
        watch2.stop();
        System.out.println("num4 = {" + num2 + "} and classic random nanos: "
                + watch2.getTotalTimeNanos() );

    sessionsAttributes = hazelcastInstance.getMap("sessionsAttributes");
        sessionsAttributes.put(session.getId(), Map.of("username1", "password1"));
        return "home";
    }
}
