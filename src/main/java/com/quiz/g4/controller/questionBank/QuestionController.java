package com.quiz.g4.controller.questionBank;

import com.quiz.g4.entity.AnswerOption;
import com.quiz.g4.entity.Lesson;
import com.quiz.g4.entity.QuestionBank;
import com.quiz.g4.entity.Subject;
import com.quiz.g4.service.AnswerOptionService;
import com.quiz.g4.service.QuestionBankService;
import com.quiz.g4.service.impl.LessonServiceImpl;
import com.quiz.g4.service.impl.SubjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class QuestionController {

    @Autowired
    private QuestionBankService questionBankService;

    @Autowired
    private AnswerOptionService answerOptionService;

    @Autowired
    private SubjectServiceImpl subjectService;

    @Autowired
    private LessonServiceImpl lessonService;

    @GetMapping("/questionlist")
    public String questions (
            @RequestParam(defaultValue = "0") int page,  // Default to first page
            @RequestParam(defaultValue = "15") int size,  // Default page size of 5
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<QuestionBank> questionPage = questionBankService.allQuestions(pageable);
        List<Subject> subjects = subjectService.getAllSubjects();
        List<Lesson> lessons = lessonService.getAllLessons();
        model.addAttribute("subjects", subjects);
        model.addAttribute("lessons", lessons);

        model.addAttribute("questionPage", questionPage);
        return "/QuestionBank/question_bank";
    }

//    @GetMapping("/search_questions")
//    public String search_questions (
//            @RequestParam(required = false) String questionContent,
//            @RequestParam(required = false) String questionType,
//            @RequestParam(required = false) Integer subject,
//            @RequestParam(required = false) Integer lesson,
//            @RequestParam(defaultValue = "0") int page,  // Default to first page
//            @RequestParam(defaultValue = "15") int size,  // Default page size of 5
//            Model model) {
//
//            Pageable pageable = PageRequest.of(page, size);
//            Page<QuestionBank> questionPage = questionBankService.searchQuestion( questionContent, questionType, subject, lesson, pageable);
//            model.addAttribute("questionPage", questionPage);
//            model.addAttribute("questionContent", questionContent);
//            model.addAttribute("questionType", questionType);
//            model.addAttribute("selectedSubjectId", subject);
//            model.addAttribute("selectedLessonId", lesson);
//            List<Subject> subjects = subjectService.getAllSubjects();
//            List<Lesson> lessons = lessonService.getAllLessons();
//            model.addAttribute("subjects", subjects);
//            model.addAttribute("lessons", lessons);
//
//
//        return "expert_manage_question";
//    }

    @GetMapping("/add_Question")
    public String addQuestion(Model model){
        List<Subject> subjects = subjectService.getAllSubjects();
        List<Lesson> lessons = lessonService.getAllLessons();
        model.addAttribute("subjects", subjects);
        model.addAttribute("lessons", lessons);
        return "/QuestionBank/addQuestion";
    }

    @PostMapping("/createQuestions")
    public String createQuestion (Model model,
                                  @RequestParam String questionContent,
                                  @RequestParam String questionType,
                                  @RequestParam int subject,
                                  @RequestParam int lesson,
                                  @RequestParam(required = false) List<String> answerContent,
                                  @RequestParam(required = false) List<Boolean> answerIsCorrect
    ) {

        // Set default values if parameters are null
        if (answerContent == null) {
            answerContent = new ArrayList<>();
        }
        if (answerIsCorrect == null) {
            answerIsCorrect = new ArrayList<>();
        }


        // Kiểm tra xem câu hỏi đã tồn tại chưa
        Set<String> setContent = new HashSet<>();
        for (int i = 0; i < answerContent.size(); i++){
            setContent.add(answerContent.get(i));
        }
        boolean correct = false;
        for (int i = 0; i < answerIsCorrect.size(); i++){
            if(answerIsCorrect.get(i) == true){
                correct = true;
                break;
            }
        }
        if(setContent.size() < answerContent.size() || !correct || answerContent.isEmpty() || answerIsCorrect.isEmpty()){
            List<Subject> subjects = subjectService.getAllSubjects();
            List<Lesson> lessons = lessonService.getAllLessons();
            model.addAttribute("content", questionContent);
            model.addAttribute("type", questionType);
            model.addAttribute("selectedSubjectId", subject);
            model.addAttribute("selectedLessonId", lesson);
            model.addAttribute("subjects", subjects);
            model.addAttribute("lessons", lessons);
            model.addAttribute("error", "đáp án bị trùng hoặc không có đáp án đúng vui lòng thử lại!");
            return "/QuestionBank/addQuestion";
        } else if (questionBankService.existsByQuestionContent(questionContent)){
            List<Subject> subjects = subjectService.getAllSubjects();
            List<Lesson> lessons = lessonService.getAllLessons();
            model.addAttribute("content", questionContent);
            model.addAttribute("type", questionType);
            model.addAttribute("selectedSubjectId", subject);
            model.addAttribute("selectedLessonId", lesson);
            model.addAttribute("subjects", subjects);
            model.addAttribute("lessons", lessons);
            model.addAttribute("error", "câu hỏi đã tồn tại!");
            return "/QuestionBank/addQuestion";
        }


            // Tạo QuestionBank
        QuestionBank questionBank = new QuestionBank();
        questionBank.setQuestionContent(questionContent);
        questionBank.setQuestionType(questionType);
        questionBank.setSubject(subjectService.findById(subject));
        questionBank.setLesson(lessonService.getLessonById(lesson));
        questionBankService.save(questionBank);


        // Tạo AnswerOption từ form
        Set<AnswerOption> answerOptions = new HashSet<>();
        for (int i = 0; i < answerContent.size(); i++) {
            AnswerOption option = new AnswerOption();
            option.setQuestionBank(questionBank);
            option.setContent(answerContent.get(i));
            if(answerIsCorrect.get(i) != null){
                option.setIsCorrect(true);
            }else{
                option.setIsCorrect(true);
            }// Xác định đáp án đúng
            answerOptionService.save(option);
        }

        return "redirect:/expert/expert_manage_question";  // Chuyển hướng sau khi lưu xong
    }

    @GetMapping("/question/{id}")
    public String viewQuestion(@PathVariable Integer id, Model model) {
        QuestionBank question = questionBankService.findById(id);
        model.addAttribute("question", question);
        return "/QuestionBank/UpdateQuestion"; // Trang hiển thị thông tin câu hỏi
    }

    @PostMapping("/question/updateQuestion")
    public String updateQuestion(Model model,
                                 @RequestParam("id") Integer id,
                                 @RequestParam("questionContent")  String questionContent,
                                 @RequestParam("questionType") String questionType
    ) {
        // Tìm câu hỏi hiện tại
        QuestionBank questionBank = questionBankService.findById(id);

        if (questionBank == null) {
            return "redirect:/questionlist"; // Hoặc hiển thị thông báo không tìm thấy
        }

        // Cập nhật thông tin câu hỏi
        questionBank.setQuestionContent(questionContent);
        questionBank.setQuestionType(questionType);
        questionBankService.save(questionBank);

        /*//Cập nhật các câu trả lời
        for (AnswerOptionForm answerOptionForm : questionForm.getAnswerOptions()) {
            // Kiểm tra xem câu trả lời đã tồn tại chưa, nếu chưa thì thêm mới
            AnswerOption answerOption = answerOptionService.findByContentAndQuestion(questionBank, answerOptionForm.getContent());
            if (answerOption == null) {
                answerOption = new AnswerOption();
                answerOption.setQuestionBank(questionBank);
            }
            answerOption.setContent(answerOptionForm.getContent());
            answerOption.setIsCorrect(answerOptionForm.getCorrect());
            answerOptionService.save(answerOption);
        }*/

        return "redirect:/questionlist";  // Chuyển hướng sau khi cập nhật xong
    }



}



