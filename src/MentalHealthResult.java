public class MentalHealthResult {

    public String getStatus(int totalScore) {
        if (totalScore <= 10)      return "✅ Good Mental Health";
        else if (totalScore <= 17) return "⚠️ Moderate Stress";
        else                       return "🔴 High Stress - Seek Help";
    }

    public String getSuggestion(int totalScore) {
        if (totalScore <= 10) {
            return "Keep up your healthy habits! Stay active and social.";
        } else if (totalScore <= 17) {
            return "Try meditation, reduce screen time, and talk to a friend.";
        } else {
            return "Please talk to a counselor or mental health professional.";
        }
    }

    public String getStatusColor(int totalScore) {
        if (totalScore <= 10)      return "green";
        else if (totalScore <= 17) return "orange";
        else                       return "red";
    }
}