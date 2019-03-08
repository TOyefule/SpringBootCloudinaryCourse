package com.example.demo;


import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CloudinaryConfig cloudc;


    @RequestMapping("/")
    public String listCourses(Model model){
        model.addAttribute("courses", courseRepository.findAll());
        return "list";
    }


    @GetMapping("/add")
    public String courseForm(Model model) {
        model.addAttribute("course", new Course());
        return "courseform";
    }

    @PostMapping("/add")
    public String processForm(@ModelAttribute Course course,
                              @RequestParam("file")MultipartFile file){
        if (file.isEmpty()){
            return "redirect:/add";
        }
        try {
            Map uploadResult= cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            course.setPhoto(uploadResult.get("url").toString());
            courseRepository.save(course);
        } catch (IOException e){
            e.printStackTrace();
            return "redirect:/add";
        }

        return "redirect:/";
    }


//    @PostMapping("/process")
//    public String processForm(@Valid Course course, BindingResult result){
//        if (result.hasErrors()){
//            return "courseform";
//        }
//        courseRepository.save(course);
//        return "redirect:/";
//    }

    @RequestMapping("/detail/{id}")
    public String showCourse(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("course", courseRepository.findById(id).get());
        return "show";
    }


    @RequestMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") long id, Model model){
        model.addAttribute("course", courseRepository.findById(id).get());
        return "courseform";
    }


    @RequestMapping("/delete/{id}")
    public String delCourse(@PathVariable("id") long id){
        courseRepository.deleteById(id);
        return "redirect:/";
    }
}
