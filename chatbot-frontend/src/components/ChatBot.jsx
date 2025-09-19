import React, { useState, useEffect, useRef } from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import "./ChatBot.css"; 

const ChatBot = () => {
  const [input, setInput] = useState("");
  const [messages, setMessages] = useState([]);
  const chatEndRef = useRef(null);

  const sendMessage = async () => {
    if (!input.trim()) return;

    const userMessage = { text: input, sender: "user" };
    setMessages([...messages, userMessage]);
    setInput("");

    try {
      const res = await axios.post("http://localhost:8080/api/chat", { prompt: input }, {
        headers: { "Content-Type": "application/json" },
      });

      const botMessage = { text: res.data, sender: "bot" };
      setMessages([...messages, userMessage, botMessage]);
    } catch (error) {
      console.error("Error fetching response", error);
      setMessages([...messages, userMessage, { text: "Error retrieving response", sender: "bot" }]);
    }
  };

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  return (
    <div className="container mt-5">
      <div className="card shadow-lg">
        <div className="card-header bg-primary text-white text-center">
          <h4>Chatbot</h4>
        </div>
        <div className="card-body chat-box" style={{ height: "400px", overflowY: "auto" }}>
          {messages.map((msg, index) => (
            <div key={index} className={`msg-bubble ${msg.sender}`}>
              {msg.text}
            </div>
          ))}
          <div ref={chatEndRef} />
        </div>
        <div className="card-footer">
          <div className="input-group">
            <input
              type="text"
              className="form-control"
              placeholder="Type your message..."
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && sendMessage()}
            />
            <button className="btn btn-primary" onClick={sendMessage}>
              Send
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ChatBot;
