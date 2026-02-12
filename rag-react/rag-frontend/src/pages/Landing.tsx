import { useNavigate } from 'react-router-dom';
import { useEffect, useRef } from 'react';

const Landing = () => {
  const navigate = useNavigate();
  const starfieldRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const container = starfieldRef.current;
    if (!container) return;

    // Clear existing stars
    container.innerHTML = '';

    // Create multiple layers of stars with varying sizes, brightness, and movement
    const layers = [
      { 
        count: 120, 
        minSize: 2, 
        maxSize: 3, 
        minOpacity: 0.6, 
        maxOpacity: 0.9, 
        color: 'rgb(255 255 255)',
        speed: 0.2,
        glow: true
      }, // Bright small stars
      { 
        count: 80, 
        minSize: 3, 
        maxSize: 4.5, 
        minOpacity: 0.7, 
        maxOpacity: 1, 
        color: 'rgb(255 255 255)',
        speed: 0.15,
        glow: true
      }, // Medium bright stars
      { 
        count: 40, 
        minSize: 4, 
        maxSize: 6, 
        minOpacity: 0.8, 
        maxOpacity: 1, 
        color: 'rgb(255 255 255)',
        speed: 0.1,
        glow: true
      }, // Large bright stars
      { 
        count: 30, 
        minSize: 1.5, 
        maxSize: 2.5, 
        minOpacity: 0.4, 
        maxOpacity: 0.6, 
        color: 'rgb(191 219 254)',
        speed: 0.25,
        glow: false
      }, // Distant blue stars
    ];

    const stars: Array<{ element: HTMLDivElement; x: number; y: number; speedX: number; speedY: number }> = [];

    layers.forEach((layer) => {
      for (let i = 0; i < layer.count; i++) {
        const star = document.createElement('div');
        const size = Math.random() * (layer.maxSize - layer.minSize) + layer.minSize;
        const x = Math.random() * 100;
        const y = Math.random() * 100;
        const opacity = Math.random() * (layer.maxOpacity - layer.minOpacity) + layer.minOpacity;
        const speedX = (Math.random() - 0.5) * layer.speed;
        const speedY = (Math.random() - 0.5) * layer.speed;

        star.style.position = 'absolute';
        star.style.width = `${size}px`;
        star.style.height = `${size}px`;
        star.style.left = `${x}%`;
        star.style.top = `${y}%`;
        star.style.backgroundColor = layer.color;
        star.style.borderRadius = '50%';
        star.style.opacity = `${opacity}`;
        star.style.pointerEvents = 'none';
        star.style.transition = 'opacity 0.5s ease';
        
        if (layer.glow) {
          star.style.boxShadow = `0 0 ${size * 2}px ${size * 0.5}px rgba(255, 255, 255, ${opacity * 0.5})`;
        }

        container.appendChild(star);
        stars.push({ element: star, x, y, speedX, speedY });
      }
    });

    // Animate stars with slow, cinematic drift
    let animationId: number;
    let frameCount = 0;
    
    const animate = () => {
      frameCount++;
      // Update every 2 frames for slower motion
      if (frameCount % 2 === 0) {
        stars.forEach((star) => {
          star.x += star.speedX;
          star.y += star.speedY;
          
          // Wrap around edges
          if (star.x > 100) star.x = 0;
          if (star.x < 0) star.x = 100;
          if (star.y > 100) star.y = 0;
          if (star.y < 0) star.y = 100;
          
          star.element.style.left = `${star.x}%`;
          star.element.style.top = `${star.y}%`;
        });
      }
      
      animationId = requestAnimationFrame(animate);
    };
    
    animate();

    return () => {
      if (animationId) {
        cancelAnimationFrame(animationId);
      }
    };
  }, []);

  const handleLoginClick = () => {
    navigate('/login');
  };

  return (
    <div className="min-h-screen flex flex-col relative overflow-hidden" style={{ 
      background: 'linear-gradient(to bottom, #0a0e27 0%, #1a1f3a 50%, #0f1419 100%)'
    }}>
      {/* Starfield Background */}
      <div
        ref={starfieldRef}
        className="absolute inset-0 w-full h-full pointer-events-none"
        style={{ zIndex: 0 }}
      />
      {/* Header */}
      <header className="w-full py-8 px-4 sm:px-6 lg:px-8 relative z-10">
        <div className="max-w-5xl mx-auto">
          <h1 className="text-2xl font-semibold text-white">yourAI</h1>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex-1 flex items-center justify-center px-4 sm:px-6 lg:px-8 py-20 relative z-10">
        <div className="w-full max-w-4xl mx-auto">
          {/* Content Container with subtle border and shadow */}
          <div className="bg-white rounded-lg border border-slate-200 shadow-xl p-12 sm:p-16 lg:p-20">
            <div className="max-w-3xl mx-auto text-center space-y-10">
              {/* Headline */}
              <div className="space-y-4">
                <h2 className="text-4xl sm:text-5xl lg:text-6xl font-bold text-slate-900 leading-tight tracking-tight">
                  Dökümanlarınızdan
                  <br />
                  <span className="text-indigo-600">Bilgi Çıkarın</span>
                </h2>
              </div>

              {/* Value Propositions */}
              <div className="space-y-4 max-w-2xl mx-auto">
                <p className="text-lg sm:text-xl text-slate-600 leading-relaxed">
                  PDF belgelerinizi yükleyin. Sorularınızı sorun. Anında yanıt alın.
                </p>
                <p className="text-base sm:text-lg text-slate-500 leading-relaxed">
                  RAG teknolojisi ile belgelerinizden doğru bilgiye hızlıca erişin.
                </p>
              </div>

              {/* CTA Button */}
              <div className="pt-6">
                <button
                  onClick={handleLoginClick}
                  className="px-8 py-3.5 bg-indigo-600 text-white font-medium text-base rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors duration-200"
                >
                  Giriş Yap
                </button>
              </div>
            </div>
          </div>
        </div>
      </main>

      {/* Footer */}
      <footer className="w-full py-8 px-4 sm:px-6 lg:px-8 border-t border-white/10 bg-white/5 backdrop-blur-sm relative z-10">
        <div className="max-w-5xl mx-auto text-center">
          <p className="text-sm text-white/70">
            Güvenli. Hızlı. Güvenilir.
          </p>
        </div>
      </footer>
    </div>
  );
};

export default Landing;
