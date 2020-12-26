package com.neomata.skelyserver.server.evaluate

object SubmissionEvaluator {
  case class SubmissionParameters(
    q1: String,
    q2: String,
    q3: String,
    q4: String,
    q5: String,
    q6: String,
    q7: String,
    q8: String,
    q9: String,
    q10: String
  )

  case class SubmissionResults(score: Double) {
    val level: String = score match {
      case absolutionist if absolutionist > 950 => "Absolutionist"
      case transcendent if transcendent > 900 && transcendent <= 950 => "Transcendent"
      case glorious if glorious > 850 && glorious <= 900 => "Glorious"
      case guru if guru > 800 && guru <= 850 => "Guru"
      case incomparable if incomparable > 750 && incomparable <= 800 => "Incomparable"
      case competent if competent > 650 && competent <= 750 => "Competent"
      case decent if decent > 550 && decent <= 650 => "Decent"
      case enough if enough > 500 && enough <= 550 => "Enough"
      case ignorable if ignorable > 450 && ignorable <= 500 => "Ignorable"
      case runt if runt > 350 && runt <= 450 => "Runt"
      case pathetic if pathetic >= 200 && pathetic <= 350 => "Pathetic"
      case atrocious if atrocious < 200 => "Atrocious"
    }
  }
}

class SubmissionEvaluator {
  import SubmissionEvaluator._

  def evaluate(sp: SubmissionParameters): SubmissionResults = {
    val q1 = sp.q1 match {
      case stall if stall == "Stall" => 100
      case refuse if refuse == "Refuse" => 40
      case comply if comply == "Comply" => 40
    }

    val q2 = sp.q2.toInt match {
      case correct if correct >= 70 && correct <= 90 => 100
      case arrogant if arrogant > 90 => 60
      case doubtful if doubtful < 70 => 30
    }

    val q3 = sp.q3 match {
      case s if s == "S" => 0
      case h if h == "H" => 50
      case u if u == "U" => 100
      case t if t == "T" => 100
      case x if x == "X" => 50
    }

    val q4 = sp.q4 match {
      case time =>
        val (minutes, seconds) = {
          val array = time.replaceAll("\\(", "").replaceAll("\\)", "").split(":")
          (array.head, array.last)
        }
        println(s"minutes: $minutes, seconds: $seconds")
        val total = (minutes.toInt * 60 + seconds.toInt).toInt
        if (total <= 390) {
          100
        } else if (total >= 900) {
          0
        } else {
          val range = 900 - 390
          val ratio = (total - 390).toDouble / range
          math.abs(ratio - 100)
        }
    }

    val q5 = sp.q5.toInt match {
      case almighty if almighty >= 50 => 100
      case weakling if weakling < 0 => 0
      case average => average * 2
    }

    val q6 = sp.q6.toDouble match {
      case time if time <= 45.0 => 100
      case time if time > 145.0 => 0
      case time => (45.0 - time) + 100
    }

    val q7 = sp.q7 match {
      case hyper if hyper == "Hyper" => 25
      case aware if aware == "Aware" => 100
      case intel if intel == "Intelligent" => 60
      case optimistic if optimistic == "Optimistic" => 45
    }

    val q8 = sp.q8 match {
      case intimidation if intimidation == "Intimidation" => 65
      case logos if logos == "Logos" => 45
      case pathos if pathos == "Pathos" => 35
      case ethos if ethos == "Ethos" => 100
    }

    val q9 = sp.q9.toDouble match {
      case time if time >= 90.0 => 100
      case time if time < 30 => 0
      case time => (time / 90.0) + 100
    }

    val q10 = sp.q10 match {
      case time if time == "Time" => 100
      case torch if torch == "Torch" => 35
      case offense if offense == "Offense" => 25
      case sneak if sneak == "Sneak" => 35
      case smart if smart == "Smart" => 55
      case _ => 0
    }

    val score = q1 + q2 + q3 + q4 + q5 + q6 + q7 + q8 + q9 + q10
    println(s"q1: $q1, q2: $q2, q3: $q3, q4: $q4, q5: $q5, q6: $q6, q7: $q7, q8: $q8, q9: $q9, q10: $q10")
    SubmissionResults(score)
  }
}