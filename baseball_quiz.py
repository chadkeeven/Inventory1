blank_answers = ["___1___","___2___","___3___","___4___","___5___"]

easy_quiz = "In baseball there are four ___1___. Touching all of the ___1___ equals one ___2___. \
There are nine ___3___s. Each team gets three ___4___s per ___3___."

medium_quiz = "A batter is given three ___1___s before they're called out. If a batter gets four ___2___s it's called a ___3___. \
___4___ ___2___s are considered ___1___s, unless there are already two. "

hard_quiz = "The team hitting can score ___1___s different ways. One way is to ___2___ a ___3___, this is where the ball goes over the fence. \
Another is to get a ___2___ with ___4___ on base. The most difficult way to score is to ___5___ home."

easy_answers = ["bases", "run", "inning", "out"]
medium_answers = ["strike", "ball", "walk", "Foul"]
hard_answers = ["run","hit","homerun", "runners", "steal"]

intro = "***Baseball Quiz*** \n1 - easy \n2 - medium \n3 - hard"

print intro
user_input = raw_input("Choose your quiz level:")

def difficulty(quiz_number):
    """
    based off the user input the correct quiz is returned
    takes in the user_input and tests what quiz number is selected
    returns the quiz that was selected. If one of 3 options isn't in the quiz_number
    varible then it returns None and loops through the question again.
    """
    quiz = ""
    if quiz_number == "1":
        quiz = easy_quiz
        return quiz
    elif quiz_number == "2":
        quiz = medium_quiz
        return quiz
    elif quiz_number == "3":
        quiz = hard_quiz
        return quiz
    else:
        return None

def answers_to_quiz(diff_quiz):
    """
    uses the quiz that was picked out to match up the correct list of answers to that quiz.
    takes in the quiz that was selected, through the def difficulty(quiz_number) function.
    returns the answers that are meant for the input quiz
    """
    answers = ""
    if diff_quiz == easy_quiz:
        answers = easy_answers
    elif diff_quiz == medium_quiz:
        answers = medium_answers
    elif diff_quiz == hard_quiz:
        answers = hard_answers
    return answers

def check_answer(guess_anw,question_num):
    """
    takes in a guess and returns if the guess was correct or not.
    the input is the answer the user guessed and the question for the number.
    it returns whether or not the guess_answer was true.
    """
    answer = list_answers[question_num - 1]
    if guess_anw == answer:
        return True
    return answer

def word_in_blank(word, blank_answers_item):
    """
    checks to see in element in blank_answers is in the current word.
    takes in the current word and compares it to the blank_answers element that is being replaced.
    returns the just the blank_answers element if it's in the word.
    """
    if blank_answers_item in word:
            return blank_answers_item
    return None
    
def replace_blank(correct_answer, current_quiz, current_question):
    """
    takes the correct answer and replaces the blank with it.
    input is the correct answer, the quiz that is selected, and the question.
    returns the current quiz with the correct answer replacing the blank space that was being asked in the question.
    """
    replaced = []
    current_quiz = current_quiz.split()
    for word in current_quiz:
        replacement = word_in_blank(word, blank_answers[current_question - 1])
        if replacement == blank_answers[current_question - 1]:
            word = word.replace(replacement,correct_answer)
            replaced.append(word)
        else:
            replaced.append(word)
    replaced = " ".join(replaced)
    return replaced

def ask_questions():
    """
    asks questions, makes the program come all together :)
    updates the quiz as the user gets questions right, tells user to try
    again if the answer is incorrect.
    """
    index = 1
    updated_quiz = chosen_quiz
    while index < len(answers_to_quiz(chosen_quiz)) + 1:
        answer_input = raw_input("Question " + str(index) +":")
        if check_answer(answer_input, index) == True:
            print "Correct!"
            updated_quiz = replace_blank(answer_input, updated_quiz, index)
            print updated_quiz
            index = index + 1
        else:
            print "Try again!"
            index = index

while difficulty(user_input) == None:
    print intro
    user_input = raw_input("Choose your quiz level:")
    
chosen_quiz = difficulty(user_input)
list_answers = answers_to_quiz(chosen_quiz)
print chosen_quiz

ask_questions()
















