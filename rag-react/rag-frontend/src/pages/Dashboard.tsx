import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context';
import apiClient from '../services/axios';
import UploadPDF from '../components/UploadPDF';

// 1. Tipi 'number' olarak güncelledik (Backend Long döndüğü için)
interface PDFDocument {
  id: number; 
  title: string;
  createdAt: string;
  [key: string]: unknown;
}

const Dashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [pdfs, setPdfs] = useState<PDFDocument[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchPDFs();
  }, []);

  const fetchPDFs = async () => {
    try {
      setIsLoading(true);
      setError('');
      // Backend'den gelen veriyi PDFDocument[] olarak karşılıyoruz
      const response = await apiClient.get<PDFDocument[]>('/rag-document/current-user');
      setPdfs(response.data);
    } catch (err) {
      console.error('Failed to fetch PDFs:', err);
      setError('PDF listesi yüklenirken bir hata oluştu.');
    } finally {
      setIsLoading(false);
    }
  };

  // 2. Parametre tipini de 'number' yaptık
  const handleDelete = async (id: number) => {
    if (!window.confirm('Bu PDF\'yi silmek istediğinizden emin misiniz?')) {
      return;
    }

    try {
      // Axios şablon dizisi (backtick) içinde sayıları otomatik stringe çevirir
      await apiClient.delete(`/rag-document/${id}`);
      
      // 3. Filtrelemede artık 'number === number' kontrolü yapılıyor
      setPdfs(pdfs.filter((pdf) => pdf.id !== id));
    } catch (err) {
      console.error('Failed to delete PDF:', err);
      alert('PDF silinirken bir hata oluştu.');
    }
  };

  const handleLogout = async () => {
    // AuthContext'ten gelen logout fonksiyonunu çağır (kullanıcı state'ini temizlemek için)
    logout();
    
    // localStorage'dan token'ı tamamen sil
    localStorage.removeItem('token');
    
    // Kullanıcıyı login sayfasına yönlendir
    navigate('/login');
  };

  const formatDate = (dateString: string) => {
    if (!dateString) return "Tarih yok"; // Ekstra güvenlik
    const date = new Date(dateString);
    return date.toLocaleDateString('tr-TR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto py-12 px-4 sm:px-6 lg:px-8">
        <UploadPDF onUploadSuccess={fetchPDFs} />
        
        <div className="bg-white shadow rounded-lg p-6 mb-6">
          <div className="flex justify-between items-center mb-6">
            <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
            <button
              onClick={handleLogout}
              className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 transition-all duration-200"
            >
              Çıkış Yap
            </button>
          </div>
          
          {user && (
            <div className="border-t border-gray-200 pt-6">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Kullanıcı Bilgileri</h3>
              <dl className="grid grid-cols-1 gap-x-4 gap-y-6 sm:grid-cols-2">
                <div>
                  <dt className="text-sm font-medium text-gray-500">Kullanıcı Adı</dt>
                  <dd className="mt-1 text-sm text-gray-900">{user.username}</dd>
                </div>
                <div>
                  <dt className="text-sm font-medium text-gray-500">Email</dt>
                  <dd className="mt-1 text-sm text-gray-900">{user.email}</dd>
                </div>
              </dl>
            </div>
          )}
        </div>

        <div className="bg-white shadow rounded-lg p-6">
          <div className="flex justify-between items-center mb-6">
            <div className="flex-1">
              <h2 className="text-2xl font-bold text-gray-900">PDF Belgelerim</h2>
              {pdfs.length === 0 && !isLoading && (
                <p className="text-sm text-amber-600 mt-1">Sohbete başlamak için önce bir PDF yükleyin</p>
              )}
            </div>
            <div className="flex items-center gap-3">
              <button
                onClick={() => navigate('/chat')}
                disabled={pdfs.length === 0}
                className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200"
              >
                Sohbete Başla
              </button>
              <button
                onClick={fetchPDFs}
                className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all duration-200"
              >
                Yenile
              </button>
            </div>
          </div>

          {isLoading ? (
            <div className="text-center py-12">
              <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
              <p className="mt-4 text-gray-600">Yükleniyor...</p>
            </div>
          ) : error ? (
            <div className="rounded-md bg-red-50 p-4">
              <h3 className="text-sm font-medium text-red-800">{error}</h3>
            </div>
          ) : pdfs.length === 0 ? (
            <div className="text-center py-12 text-gray-500">Henüz PDF belgeniz bulunmamaktadır.</div>
          ) : (
            <div className="overflow-hidden">
              <ul className="divide-y divide-gray-200">
                {pdfs.map((pdf) => (
                  <li key={pdf.id} className="py-4"> 
                    <div className="flex items-center justify-between">
                      <div className="flex-1 min-w-0">
                        <p className="text-sm font-medium text-gray-900 truncate">{pdf.title}</p>
                        <p className="text-sm text-gray-500">{formatDate(pdf.createdAt)}</p>
                      </div>
                      <div className="ml-4 flex-shrink-0">
                        <button
                          onClick={() => handleDelete(pdf.id)}
                          className="px-4 py-2 bg-red-600 text-white text-sm rounded-md hover:bg-red-700"
                        >
                          Sil
                        </button>
                      </div>
                    </div>
                  </li>
                ))}
              </ul>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;