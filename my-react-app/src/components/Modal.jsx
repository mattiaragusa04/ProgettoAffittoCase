import React from 'react'

function Modal({ show, onClose, title, message, type = 'info' }) {
  if (!show) return null;

  const isError = type === 'error';
  const isSuccess = type === 'success';

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4">
        <div className="flex flex-col items-center bg-white shadow-xl rounded-xl py-6 px-5 w-full max-w-md border border-gray-200 animate-fade-in">
            <div className={`flex items-center justify-center p-4 rounded-full ${isError ? 'bg-red-100' : isSuccess ? 'bg-green-100' : 'bg-blue-100'}`}>
                {isError ? (
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M18 6L6 18M6 6l12 12" stroke="#DC2626" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                    </svg>
                ) : isSuccess ? (
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#16A34A" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <path d="M20 6 9 17l-5-5"/>
                    </svg>
                ) : (
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#2563EB" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <circle cx="12" cy="12" r="10"/>
                        <path d="M12 16v-4"/>
                        <path d="M12 8h.01"/>
                    </svg>
                )}
            </div>
            <h2 className="text-gray-900 font-semibold mt-4 text-xl">{title}</h2>
            <p className="text-sm text-gray-600 mt-2 text-center">
                {message}
            </p>
            <div className="flex items-center justify-center gap-4 mt-5 w-full">
                <button onClick={onClose} type="button" className={`w-full md:w-36 h-10 rounded-md text-white font-medium text-sm active:scale-95 transition ${isError ? 'bg-red-600 hover:bg-red-700' : isSuccess ? 'bg-green-600 hover:bg-green-700' : 'bg-blue-600 hover:bg-blue-700'}`}>
                    {isError ? 'Chiudi' : 'OK'}
                </button>
            </div>
        </div>
    </div>
  )
}

export default Modal
