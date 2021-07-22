import application.controller.AppRestController;
import application.entity.WordsList;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableWebMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppRestController.class)
public class AppRestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    ObjectMapper om = new ObjectMapper();

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    /*Тесты должны запускаться при сборке проекта через
    maven, если тесты не пройдены - сборка должна зафейлится*/
//    @Test
//    public void mavenPackageFailureTest()  {
//        Assert.fail();
//    }


    /*Пример 1
    {
        “words”: [
                “fish”,
                “horse”,
                “egg”’,
                “goose”,
                “eagle”
        ]
    }
    Будет возвращено
    {
        “words”: [
                “fish”,
                “horse”,
                “egg”’,
                “goose”,
                “eagle”
        ]
    }*/
    @Test
    public void example1Test() throws Exception {
        WordsList input = new WordsList(new String[]{"fish", "horse", "egg", "goose", "eagle"});
        WordsList expectedOutput = new WordsList(new String[]{"fish", "horse", "egg", "goose", "eagle"});

        String jsonAsStringRequest = om.writeValueAsString(input);
        String jsonAsStringExpectedOutput = om.writeValueAsString(expectedOutput);

        MvcResult result = mockMvc.perform(post("/api/words").content(jsonAsStringRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        Assert.assertEquals(jsonAsStringExpectedOutput,resultContent);
    }


    /*Пример 2
    {
        “words”: [
                “fish”,
                “horse”,
                “snake”’,
                “goose”,
                “eagle”
        ]
    }
    Будет возвращено
    {
        “words”: [
                “fish”,
                “horse”,
        ]
    }
    Поскольку “horse” заканчивается на “e”, а “snake” начинается на “s”
    В случае если игра закончена досрочно с ошибкой в слове, необходимо вернуть
    список который содержит все валидные объекты массива до ошибки. */
    @Test
    public void example2Test() throws Exception {
        WordsList input = new WordsList(new String[]{"fish", "horse", "snake", "goose", "eagle"});
        WordsList expectedOutput = new WordsList(new String[]{"fish", "horse"});

        String jsonAsStringRequest = om.writeValueAsString(input);
        String jsonAsStringExpectedOutput = om.writeValueAsString(expectedOutput);

        MvcResult result = mockMvc.perform(post("/api/words").content(jsonAsStringRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        Assert.assertEquals(jsonAsStringExpectedOutput,resultContent);
    }

    /*Пример 3
    {
        “words”: [
                “fish”,
                “horse”,
                “”’,
                “goose”,
                “eagle”
        ]
    }
    Будет возвращено
    {
        “words”: [
                “fish”,
                “horse”,
        ]
    }
    Пустая строка в массиве считается невалидной (ошибкой в слове).*/

    @Test
    public void example3Test() throws Exception {
        WordsList input = new WordsList(new String[]{"fish", "horse", "", "goose", "eagle"});
        WordsList expectedOutput = new WordsList(new String[]{"fish", "horse"});

        String jsonAsStringRequest = om.writeValueAsString(input);
        String jsonAsStringExpectedOutput = om.writeValueAsString(expectedOutput);

        MvcResult result = mockMvc.perform(post("/api/words").content(jsonAsStringRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        Assert.assertEquals(jsonAsStringExpectedOutput,resultContent);
    }


    /*Пример 4
    {
        “words”: [
                “fsh”,
                “horse”,
                “”’,
                “goose”,
                “eagle”
        ]
    }
Будет возвращен пустой массив.*/

    @Test
    public void example4Test() throws Exception {
        WordsList input = new WordsList(new String[]{"", "horse", "", "goose", "eagle"});
        WordsList expectedOutput = new WordsList(new String[]{});

        String jsonAsStringRequest = om.writeValueAsString(input);
        String jsonAsStringExpectedOutput = om.writeValueAsString(expectedOutput);

        MvcResult result = mockMvc.perform(post("/api/words").content(jsonAsStringRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        System.out.println(resultContent);
        Assert.assertEquals(jsonAsStringExpectedOutput,resultContent);
    }
}



