import { useState } from 'react';
import type { FormEvent, ChangeEvent } from 'react';
import apiClient from '../services/axios';

interface UploadPDFProps {
  onUploadSuccess: () => void;
}

const UploadPDF = ({ onUploadSuccess }: UploadPDFProps) => {
  const [title, setTitle] = useState('');
  const [file, setFile] = useState<File | null>(null);
  const [isUploading, setIsUploading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    const selectedFile = e.target.files?.[0];
    if (selectedFile) {
      // Sadece PDF dosyalarını kabul et
      if (selectedFile.type !== 'application/pdf') {
        setError('Lütfen sadece PDF dosyası seçin.');
        setFile(null);
        return;
      }
      setFile(selectedFile);
      setError('');
    }
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError('');
    setSuccess(false);

    if (!file) {
      setError('Lütfen bir PDF dosyası seçin.');
      return;
    }

    if (!title.trim()) {
      setError('Lütfen bir başlık girin.');
      return;
    }

    try {
      setIsUploading(true);

      // FormData oluştur
      const formData = new FormData();
      formData.append('file', file);
      formData.append('title', title);

      // multipart/form-data ile POST isteği gönder
      await apiClient.post('/rag-document', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      // Başarılı yükleme
      setSuccess(true);
      setTitle('');
      setFile(null);
      // File input'u sıfırla
      const fileInput = document.getElementById('pdf-file') as HTMLInputElement;
      if (fileInput) {
        fileInput.value = '';
      }

      // PDF listesini yenile
      onUploadSuccess();

      // Başarı mesajını 3 saniye sonra kaldır
      setTimeout(() => {
        setSuccess(false);
      }, 3000);
    } catch (err) {
      console.error('Upload failed:', err);
      setError('PDF yüklenirken bir hata oluştu. Lütfen tekrar deneyin.');
    } finally {
      setIsUploading(false);
    }
  };

  return (
    <div className="bg-white shadow rounded-lg p-6 mb-6">
      <h2 className="text-2xl font-bold text-gray-900 mb-6">PDF Yükle</h2>
      
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label htmlFor="title" className="block text-sm font-medium text-gray-700 mb-2">
            Başlık
          </label>
          <input
            id="title"
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
            placeholder="PDF başlığını girin"
            disabled={isUploading}
          />
        </div>

        <div>
          <label htmlFor="pdf-file" className="block text-sm font-medium text-gray-700 mb-2">
            PDF Dosyası
          </label>
          <input
            id="pdf-file"
            type="file"
            accept=".pdf,application/pdf"
            onChange={handleFileChange}
            className="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-indigo-50 file:text-indigo-700 hover:file:bg-indigo-100"
            disabled={isUploading}
          />
          {file && (
            <p className="mt-2 text-sm text-gray-600">
              Seçilen dosya: {file.name}
            </p>
          )}
        </div>

        {error && (
          <div className="rounded-md bg-red-50 p-4">
            <div className="flex">
              <div className="ml-3">
                <h3 className="text-sm font-medium text-red-800">{error}</h3>
              </div>
            </div>
          </div>
        )}

        {success && (
          <div className="rounded-md bg-green-50 p-4">
            <div className="flex">
              <div className="ml-3">
                <h3 className="text-sm font-medium text-green-800">
                  PDF başarıyla yüklendi!
                </h3>
              </div>
            </div>
          </div>
        )}

        <div>
          <button
            type="submit"
            disabled={isUploading || !file || !title.trim()}
            className="w-full px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {isUploading ? 'Yükleniyor...' : 'PDF Yükle'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default UploadPDF;
