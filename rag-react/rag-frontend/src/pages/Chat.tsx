import { useState, useRef, useEffect } from 'react';
import type { FormEvent } from 'react';
import apiClient from '../services/axios';

interface Message {
  id: string;
  question: string;
  answer: string;
  timestamp: Date;
}

const Chat = () => {
  const [question, setQuestion] = useState('');
  const [messages, setMessages] = useState<Message[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const chatContainerRef = useRef<HTMLDivElement>(null);

  // Yeni mesaj geldiğinde veya yükleme durumu değiştiğinde aşağı kaydır
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, isLoading]);

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    
    if (!question.trim() || isLoading) {
      return;
    }

    const userQuestion = question.trim();
    setQuestion('');
    setError('');
    setIsLoading(true);

    // 1. Kullanıcı mesajını listeye ekle (Cevap kısmı şimdilik boş)
    const newMessageId = Date.now().toString();
    const userMessage: Message = {
      id: newMessageId,
      question: userQuestion,
      answer: '',
      timestamp: new Date(),
    };
    setMessages((prev) => [...prev, userMessage]);

    try {
      // 2. Backend direkt String döndüğü için tipi <string> olarak güncelledik
      const response = await apiClient.post<string>(
        `/rag/ask?question=${encodeURIComponent(userQuestion)}`
      );

      // 3. response.data artık direkt cevabın kendisidir
      const aiAnswer = response.data;

      setMessages((prev) => 
        prev.map((msg) => 
          msg.id === newMessageId ? { ...msg, answer: aiAnswer } : msg
        )
      );
    } catch (err) {
      console.error('Failed to get AI response:', err);
      setError('Soru gönderilirken bir hata oluştu. Lütfen tekrar deneyin.');
      
      // Hata durumunda boş kalan mesajı listeden temizle
      setMessages((prev) => prev.filter((msg) => msg.id !== newMessageId));
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="h-screen flex flex-col bg-gray-50">
      <div className="flex-1 flex flex-col max-w-4xl w-full mx-auto overflow-hidden">
        {/* Mesaj Alanı */}
        <div
          ref={chatContainerRef}
          className="flex-1 overflow-y-auto px-4 sm:px-6 py-6 space-y-6"
        >
          {messages.length === 0 ? (
            <div className="flex items-center justify-center h-full">
              <p className="text-gray-400 text-lg font-medium">Size nasıl yardımcı olabilirim?</p>
            </div>
          ) : (
            messages.map((message) => (
              <div key={message.id} className="space-y-4">
                {/* Kullanıcı Mesajı */}
                <div className="flex justify-end">
                  <div className="max-w-[80%] bg-indigo-600 text-white rounded-2xl rounded-tr-sm px-4 py-3 shadow-md">
                    <p className="text-sm leading-relaxed whitespace-pre-wrap">
                      {message.question}
                    </p>
                  </div>
                </div>

                {/* AI Yanıtı - Sadece answer doluysa göster */}
                {message.answer && (
                  <div className="flex justify-start animate-fade-in">
                    <div className="max-w-[80%] bg-white border border-gray-200 rounded-2xl rounded-tl-sm px-4 py-3 shadow-sm">
                      <p className="text-sm text-gray-800 leading-relaxed whitespace-pre-wrap">
                        {message.answer}
                      </p>
                    </div>
                  </div>
                )}
              </div>
            ))
          )}

          {/* Yükleme Göstergesi */}
          {isLoading && (
            <div className="flex justify-start animate-pulse">
              <div className="bg-white border border-gray-200 rounded-2xl rounded-tl-sm px-4 py-3 shadow-sm flex items-center space-x-2">
                <div className="animate-spin rounded-full h-4 w-4 border-2 border-gray-300 border-t-indigo-600"></div>
                <span className="text-sm text-gray-500">Düşünüyor...</span>
              </div>
            </div>
          )}
          <div ref={messagesEndRef} />
        </div>

        {/* Hata Mesajı */}
        {error && (
          <div className="px-4 py-2">
            <div className="bg-red-50 border border-red-200 rounded-lg px-4 py-2 text-xs text-red-800 text-center">
              {error}
            </div>
          </div>
        )}

        {/* Giriş Alanı */}
        <div className="border-t border-gray-200 bg-white p-4 sm:p-6">
          <form onSubmit={handleSubmit} className="flex items-center space-x-3 max-w-4xl mx-auto">
            <input
              type="text"
              value={question}
              onChange={(e) => setQuestion(e.target.value)}
              placeholder="PDF'leriniz hakkında bir soru sorun..."
              className="flex-1 px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 outline-none transition-all text-sm"
              disabled={isLoading}
            />
            <button
              type="submit"
              disabled={isLoading || !question.trim()}
              className="px-6 py-3 bg-indigo-600 text-white rounded-xl hover:bg-indigo-700 disabled:opacity-50 transition-colors shadow-lg font-semibold text-sm"
            >
              Gönder
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Chat;