import axios from "axios";

export const API_BASE_URL = "http://subot.perga.co:8080";

export function askQuestion(sessionId, question) {
    return axios.post(`${API_BASE_URL}/ask`, {
        sessionId,
        question
    })
}
