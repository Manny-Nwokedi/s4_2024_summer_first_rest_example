// REST Service Application
package com.keyin;

import com.keyin.hello.Greeting;
import com.keyin.hello.GreetingService;
import com.keyin.hello.Language;
import com.keyin.hello.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class RESTServiceApplication implements CommandLineRunner {

    @Autowired
    private GreetingService greetingService;

    @Autowired
    private LanguageRepository languageRepository;

    public static void main(String[] args) {
        SpringApplication.run(RESTServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Language> languages = new ArrayList<>();
        languages.add(new Language("English"));
        languages.add(new Language("Irish"));
        languages.add(new Language("Portuguese"));
        languages.add(new Language("Polish"));

        for (Language language : languages) {
            if (languageRepository.findByName(language.getName()) == null) {
                languageRepository.save(language);
            }
        }

        List<Greeting> greetings = new ArrayList<>();
        greetings.add(createGreeting("Hello", "John", "English"));
        greetings.add(createGreeting("Dia dhuit", "Sean", "Irish"));
        greetings.add(createGreeting("Olá", "João", "Portuguese"));
        greetings.add(createGreeting("Cześć", "Jan", "Polish"));

        for (Greeting greeting : greetings) {
            greetingService.createGreeting(greeting);
        }

        List<Greeting> allGreetings = greetingService.getAllGreetings();
        allGreetings.forEach(greeting -> System.out.println(greeting.getGreeting() + " " + greeting.getName() + " in " + greeting.getLanguages().get(0).getName()));
    }

    private Greeting createGreeting(String greetingText, String name, String languageName) {
        Greeting greeting = new Greeting();
        greeting.setGreeting(greetingText);
        greeting.setName(name);

        Language language = languageRepository.findByName(languageName);
        if (language != null) {
            List<Language> languages = new ArrayList<>();
            languages.add(language);
            greeting.setLanguages(languages);
        }

        return greeting;
    }
}
