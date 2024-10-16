import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './index.css';
import { FormationProvider } from './contexts/FormationContext';
import { QueryClient, QueryClientProvider } from 'react-query';

const queryClient = new QueryClient();

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <QueryClientProvider client={queryClient}>
    <FormationProvider>
      <App />
    </FormationProvider>
  </QueryClientProvider>
);
