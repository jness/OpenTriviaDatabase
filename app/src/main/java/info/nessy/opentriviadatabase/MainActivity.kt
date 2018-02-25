package info.nessy.opentriviadatabase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.util.*

class TrivaRequest(
        val results: List<TrivaResult>
)

class TrivaResult(
        val category: String,
        val type: String,
        val difficulty: String,
        val question: String,
        val correct_answer: String,
        val incorrect_answers: List<String>
)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetch()

        nextButton.setOnClickListener {
            fetch()
        }

    }

    private fun fetch() {
        println("Fetching from open trivia database")
        val url = "https://opentdb.com/api.php?amount=1&type=multiple"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?) {
                // get response body as string
                val body = response?.body()?.string()
                // parse response body as json object
                val gson = GsonBuilder().create()
                val trivaRequest = gson.fromJson(body, TrivaRequest::class.java)

                val result = trivaRequest.results[0]
                val question = Html.fromHtml(result.question)
                val questionCategory = Html.fromHtml(result.category)
                val correctAnswer = Html.fromHtml(result.correct_answer)
                val incorrectAnswer1 = Html.fromHtml(result.incorrect_answers[0])
                val incorrectAnswer2 = Html.fromHtml(result.incorrect_answers[1])
                val incorrectAnswer3 = Html.fromHtml(result.incorrect_answers[2])

                // build list of all questions
                val questions = listOf(
                        correctAnswer,
                        incorrectAnswer1,
                        incorrectAnswer2,
                        incorrectAnswer3
                )

                // shuffle list
                Collections.shuffle(questions)


                runOnUiThread {

                    questionText.text = question
                    questionCata.text = questionCategory

                    button1.text = questions[0]
                    button1.isClickable = true

                    button2.text = questions[1]
                    button2.isClickable = true

                    button3.text = questions[2]
                    button3.isClickable = true

                    button4.text = questions[3]
                    button4.isClickable = true

                    button1.setOnClickListener {
                        button1.isClickable = false
                        if (button1.text == correctAnswer) {
                            fetch()
                        } else {
                            button1.text = "---"
                        }
                    }

                    button2.setOnClickListener {
                        button2.isClickable = false
                        if (button2.text == correctAnswer) {
                            fetch()
                        } else {
                            button2.text = "---"
                        }
                    }

                    button3.setOnClickListener {
                        button3.isClickable = false
                        if (button3.text == correctAnswer) {
                            fetch()
                        } else {
                            button3.text = "---"
                        }
                    }

                    button4.setOnClickListener {
                        button4.isClickable = false
                        if (button4.text == correctAnswer) {
                            fetch()
                        } else {
                            button4.text = "---"
                        }
                    }

                }

            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute request")
            }

        })

    }
}
