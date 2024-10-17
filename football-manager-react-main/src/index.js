// src/index.js

import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './index.css';
import { FormationProvider } from './contexts/FormationContext';
import { QueryClient, QueryClientProvider } from 'react-query';
import { AuthProvider } from './contexts/AuthContext'; 

const queryClient = new QueryClient();

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <AuthProvider> {/* Single AuthProvider */}
    <QueryClientProvider client={queryClient}> {/* Single QueryClientProvider */}
      <FormationProvider> {/* Single FormationProvider */}
        <App />
      </FormationProvider>
    </QueryClientProvider>
  </AuthProvider>
);
