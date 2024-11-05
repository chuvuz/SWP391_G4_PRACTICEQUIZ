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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public String addQuestion(Model model) {
        List<Subject> subjects = subjectService.getAllSubjects();
        List<Lesson> lessons = lessonService.getAllLessons();
        model.addAttribute("subjects", subjects);
        model.addAttribute("lessons", lessons);
        return "/QuestionBank/addQuestion";
    }

    @GetMapping("/lessonsBySubject/{subjectId}")
    @ResponseBody // Chỉ định rằng kết quả sẽ được trả về dưới dạng JSON
    public List<Lesson> getLessonsBySubject(@PathVariable Integer subjectId) {
        return lessonService.getLessonsBySubjectId(subjectId); // Lấy bài học theo môn học
    }

    @PostMapping("/createQuestions")
    public String createQuestion(Model model,
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
        for (int i = 0; i < answerContent.size(); i++) {
            setContent.add(answerContent.get(i));
        }
        boolean correct = false;
        for (int i = 0; i < answerIsCorrect.size(); i++) {
            System.out.println(answerIsCorrect.get(i).toString());
        }
        for (int i = 0; i < answerIsCorrect.size(); i++) {
            if (answerIsCorrect.get(i) == true) {
                correct = true;
                break;
            }
        }
        if (setContent.size() < answerContent.size() || !correct || answerContent.isEmpty() || answerIsCorrect.isEmpty()) {
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
        } else if (questionBankService.existsByQuestionContent(questionContent)) {
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
            option.setIsCorrect(answerIsCorrect.get(i));
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

    @PostMapping("/question/updateOption")
    public String updateOption(Model model,
                               @RequestParam("id") Integer id,
                               @RequestParam("optionId") Integer optionId,
                               @RequestParam("optionContent") String optionContent) {

        QuestionBank question = questionBankService.findById(id);

        // Kiểm tra xem câu hỏi đã tồn tại chưa
        Set<AnswerOption> setContent = question.getAnswerOptions();

        // Kiểm tra xem option đã tồn tại hay chưa
        boolean exists = setContent.stream()
                .anyMatch(option -> option.getContent().equalsIgnoreCase(optionContent));

        // Kiểm tra xem nội dung option có bị trùng không
        if (exists) {
            model.addAttribute("errorUpdateOption", "Cập nhật thất bại do nội dung đáp án trùng lặp!");
            model.addAttribute("question", questionBankService.findById(id));
            return "/QuestionBank/UpdateQuestion";
        }

        // Cập nhật nội dung đáp án
        AnswerOption option = answerOptionService.findById(optionId);
        option.setContent(optionContent);
        answerOptionService.save(option);

        model.addAttribute("question", questionBankService.findById(id));
        return "/QuestionBank/UpdateQuestion";  // Trả về trang cập nhật sau khi lưu
    }


    @PostMapping("/question/updateAnswer")
    public String updateAnswer(Model model,
                               @RequestParam("id") Integer id,
                               @RequestParam(value = "answerId", required = false) List<Integer> answerIds,
                               @RequestParam(value = "correct", required = false) List<Integer> correctAnswers,
                               @RequestParam Map<String, String> requestParams) {

        // Lấy câu hỏi từ dịch vụ
        QuestionBank question = questionBankService.findById(id);

        // Biến để theo dõi có đáp án đúng hay không
        boolean hasCorrectAnswer = false;

        // Xử lý loại câu hỏi
        if (question.getQuestionType().equals("SINGLE_CHOICE")) {
            List<AnswerOption> answerOptions = new ArrayList<>();
            // Xử lý cho SINGLE_CHOICE
            Integer selectedCorrectAnswerId = correctAnswers != null && !correctAnswers.isEmpty() ? correctAnswers.get(0) : null;

            for (Integer answerId : answerIds) {
                AnswerOption answerOption = answerOptionService.findById(answerId);
                if (answerOption != null) {
                    boolean isCorrect = answerId.equals(selectedCorrectAnswerId);
                    answerOption.setIsCorrect(isCorrect);
                    hasCorrectAnswer |= isCorrect; // Cập nhật trạng thái có đáp án đúng
                    answerOptions.add(answerOption);
                    // Lưu cập nhật
                    answerOptionService.save(answerOption);
                }
            }
            if (hasCorrectAnswer) {
                for (AnswerOption Option : answerOptions) {
                    // Lưu cập nhật
                    answerOptionService.save(Option);
                }
            }
        } else if (question.getQuestionType().equals("MULTIPLE_CHOICE")) {
            List<AnswerOption> answerOptions = new ArrayList<>();
            // Xử lý cho MULTIPLE_CHOICE
            for (int i = 0; i < answerIds.size(); i++) {
                Integer answerId = answerIds.get(i);
                AnswerOption answerOption = answerOptionService.findById(answerId);
                if (answerOption != null) {
                    // Kiểm tra tên của checkbox, sử dụng index để ánh xạ
                    String correctAnswerKey = "correct__" + i; // Tạo tên cho checkbox
                    boolean isCorrect = requestParams.containsKey(correctAnswerKey);
                    answerOption.setIsCorrect(isCorrect);
                    answerOptions.add(answerOption);
                    hasCorrectAnswer |= isCorrect; // Cập nhật trạng thái có đáp án đúng
                }
            }
            if (hasCorrectAnswer) {
                for (AnswerOption Option : answerOptions) {
                    // Lưu cập nhật
                    answerOptionService.save(Option);
                }
            }
        }

        // Kiểm tra xem có đáp án đúng hay không
        if (!hasCorrectAnswer) {
            model.addAttribute("errorUpdateAnswer", "Bạn cần chọn ít nhất một đáp án đúng.");
            model.addAttribute("question", question);
            return "/QuestionBank/UpdateQuestion"; // Quay lại trang hiện tại với thông báo lỗi
        }

        model.addAttribute("question", question);
        return "/QuestionBank/UpdateQuestion"; // Quay lại trang hiện tại
    }


    @PostMapping("/question/addOption")
    public String addOption(Model model,
                            @RequestParam("id") Integer id,
                            @RequestParam(required = false) String answerContent,
                            @RequestParam(required = false) Boolean answerIsCorrect) {

        QuestionBank question = questionBankService.findById(id);

        // Kiểm tra xem câu hỏi đã tồn tại chưa
        Set<AnswerOption> setContent = question.getAnswerOptions();

        // Kiểm tra xem option đã tồn tại hay chưa
        boolean exists = setContent.stream()
                .anyMatch(option -> option.getContent().equalsIgnoreCase(answerContent));

        if (exists) {
            // Thêm thông báo lỗi nếu option đã tồn tại
            model.addAttribute("addOptionError", "Option đã tồn tại cho câu hỏi này.");
            model.addAttribute("question", questionBankService.findById(id));
            return "/QuestionBank/UpdateQuestion";
        }

        // Nếu chưa tồn tại, thêm mới AnswerOption
        AnswerOption newOption = AnswerOption.builder()
                .content(answerContent)
                .isCorrect(answerIsCorrect)
                .questionBank(question)
                .build();

        // Lưu thay đổi vào cơ sở dữ liệu
        answerOptionService.save(newOption);

        // Trả về trang cập nhật câu hỏi
        model.addAttribute("successMessage", "Option đã được thêm thành công.");
        model.addAttribute("question", questionBankService.findById(id));
        return "/QuestionBank/UpdateQuestion";
    }

    @PostMapping("/question/updateQuestion")
    public String updateQuestion(Model model,
                                 @RequestParam("id") Integer id,
                                 @RequestParam("questionContent") String questionContent
    ) {
        // Tìm câu hỏi hiện tại
        QuestionBank question = questionBankService.findById(id);

        // Cập nhật thông tin câu hỏi
        if (questionBankService.existsByQuestionContent(questionContent)) {
            model.addAttribute("updateQuestionError", "Cập nhật thất bại do câu hỏi trùng lặp!");
            model.addAttribute("question", questionBankService.findById(id));
            return "/QuestionBank/UpdateQuestion";
        }
        question.setQuestionContent(questionContent);
        questionBankService.save(question);
        model.addAttribute("question", questionBankService.findById(id));
        return "/QuestionBank/UpdateQuestion";  // Chuyển hướng sau khi cập nhật xong
    }


}



