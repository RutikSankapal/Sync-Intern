package thesurveymanager.user_interface;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import thesurveymanager.Answer;
import thesurveymanager.Question;
import thesurveymanager.Survey;
import thesurveymanager.TheSurveyManager;

public class UserInterface extends JFrame{
    private ArrayList<Survey> survey_list;
    private Survey selected_survey;
    
    private InitialScreen initial_screen;
    private AdmScreen adm_screen;
    private ResultsScreen results_screen;
    private QuestionScreen question_screen;
    private QuestionMenuScreen question_menu_screen;
    private AddQuestionScreen add_question_screen;
    private RemoveQuestionScreen remove_question_screen;
    private ChangeAlternativeScreen change_alternative_screen;
    private HistoryScreen history_screen;
    private RemoveAnswerScreen remove_answer_screen;
    private EditQuestionScreen edit_question_screen;
    
    
    public UserInterface(ArrayList<Survey> survey_list) {
        this.survey_list = survey_list;
        this.selected_survey = survey_list.get(0);
        
        closeOperation();
        
        showInitialScreen();
        setVisible(true);
    }
    
//Getters e Setters -------------------------------------------------------------------------------------
    public Survey getSelectedSurvey() {
        return selected_survey;
    }
    
    public void setSelectedSurvey(Survey survey) {
        this.selected_survey = survey;
    }
    
    public ArrayList<Survey> getSurveyList() {
        return survey_list;
    }
//-------------------------------------------------------------------------------------------------------
    
//Screens -----------------------------------------------------------------------------------------------
    public void setPanel(JPanel panel) {
        getContentPane().removeAll();
        getContentPane().add(panel);
        pack();
    }
    
    public void showInitialScreen() {
        if(initial_screen == null) {
            initial_screen = new InitialScreen(this);
        }
        
        setPanel(initial_screen);
    }
    
    public void showAdmScreen() {
        if(adm_screen == null) {
            adm_screen = new AdmScreen(this);
        }
        
        setPanel(adm_screen);
    }
    
    public void showResultsScreen() {
        results_screen = new ResultsScreen(this);
        
        setPanel(results_screen);
    }
    
    public void showQuestionScreen() {
        if(question_screen == null) {
             if(selected_survey.getQuestions().size() <= 0) {
                System.out.println("There's no questions registered");
            }
            else {
                question_screen = new QuestionScreen(this, selected_survey.getQuestions().get(0), new Answer());
                setPanel(question_screen);
            }
        }
        
        else {
            selected_survey.getQuestions().forEach(q -> {
                if(q.getId() == question_screen.getQuestion().getId()) {
                    q.addAnswer(question_screen.getSelected());
                }
            });
        
            if(selected_survey.getQuestions().indexOf(question_screen.getQuestion()) == selected_survey.getQuestions().size() - 1) {
                selected_survey.saveAnswer(question_screen.getAnswer());
                showInitialScreen();
                question_screen = null;
            }
            else {
                question_screen = new QuestionScreen(this, selected_survey.getQuestions().get(selected_survey.getQuestions().indexOf(question_screen.getQuestion()) + 1),question_screen.getAnswer());
                setPanel(question_screen);
            }
        }
    }
    
    public void showQuestionMenuScreen() {
        question_menu_screen = new QuestionMenuScreen(this);
        setPanel(question_menu_screen);
    }
    
    public void showAddQuestionScreen() {
        add_question_screen = new AddQuestionScreen(this);
        setPanel(add_question_screen);
    }
    
    public void  showRemoveQuestionScreen() {
        remove_question_screen = new RemoveQuestionScreen(this);
        setPanel(remove_question_screen);
    }
    
    public void showChangeAlternativeScreen() {
        change_alternative_screen = new ChangeAlternativeScreen(this);
        setPanel(change_alternative_screen);
    }
    
    public void showHistoryScreen() {
        history_screen = new HistoryScreen(this);
        setPanel(history_screen);
    }
    
    public void showRemoveAnswerScreen() {
        remove_answer_screen = new RemoveAnswerScreen(this);
        setPanel(remove_answer_screen);
    }
    
    public void showEditQuestionScreen(Question question) {
        selected_survey.getQuestions().forEach(q -> {
            if(q == question) {
                edit_question_screen = new EditQuestionScreen(this, q);
            }
        });
        
        setPanel(edit_question_screen);
    }
    
//----------------------------------------------------------------------------------------------------
    
//Operations -----------------------------------------------------------------------------------------
    public void addQuestionOperation() {
        selected_survey.addQuestion(add_question_screen.message, add_question_screen.a, add_question_screen.b, add_question_screen.c, add_question_screen.d, add_question_screen.e);
    }
    
    public void removeQuestionOperation() {
        selected_survey.removeQuestion(Integer.parseInt(question_menu_screen.getSelected().substring(0, 1)));
    }
    
    public void editQuestionOperation() {
        selected_survey.editQuestion(edit_question_screen.getQuestion().getId(), edit_question_screen.message);
        selected_survey.changeAlternative(edit_question_screen.getQuestion().getId(), 'a', edit_question_screen.a);
        selected_survey.changeAlternative(edit_question_screen.getQuestion().getId(), 'b', edit_question_screen.b);
        selected_survey.changeAlternative(edit_question_screen.getQuestion().getId(), 'c', edit_question_screen.c);
        selected_survey.changeAlternative(edit_question_screen.getQuestion().getId(), 'd', edit_question_screen.d);
        selected_survey.changeAlternative(edit_question_screen.getQuestion().getId(), 'e', edit_question_screen.e);
    }
    
    public void removeAnswerOperation() {
        selected_survey.removeAnswer(remove_answer_screen.id);
    }
// ---------------------------------------------------------------------------------------------------
    
//Other methods --------------------------------------------------------------------------------------    
    public void closeOperation() {
        WindowListener exitListener = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog( 
                    null, 
                    "Are you sure want to end the program??",
                    "End Program", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    null, 
                    null
                );
                if (confirm == 0) {
                    TheSurveyManager.saveOperation();
                    System.exit(0);
                }
            }
        };
        addWindowListener(exitListener);
    }
//--------------------------------------------------------------------------------------------------
    
}
