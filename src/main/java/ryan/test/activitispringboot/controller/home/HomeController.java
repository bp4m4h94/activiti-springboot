package ryan.test.activitispringboot.controller.home;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private RepositoryService repositoryService; //注入Activiti的RepositoryService

    @RequestMapping({"/", "/processes"})
    public ModelAndView home() {

        // 取出已存入DB的Model清單
        List<Model> processModels = repositoryService.createModelQuery().list();

        ModelAndView modelAndView = new ModelAndView("/processes");
        modelAndView.addObject("processModels", processModels);
        return modelAndView;
    }

}
