package ru.itpark.corporateEmail;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itpark.corporateEmail.repository.DepartmentRepository;
import ru.itpark.corporateEmail.service.MessageService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailApplicationTests {

//    TODO: Напсиать наполнение тестовыми данными + тесты
    @Autowired
    MessageService messageService;

    @Autowired
    DepartmentRepository departmentRepository;

    @Before
    void initialize(){

    }




}
