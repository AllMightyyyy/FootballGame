// index.js
import React from 'react';
import ReactDOM from 'react-dom/client'; // Import from 'react-dom/client' for React 18
import App from './App';
import { FormationProvider } from './contexts/FormationContext';
import { QueryClient, QueryClientProvider } from 'react-query';

const queryClient = new QueryClient();

// Create the root element
const root = ReactDOM.createRoot(document.getElementById('root')); // Use createRoot instead of ReactDOM.render

root.render(
  <QueryClientProvider client={queryClient}>
    <FormationProvider>
      <App />
    </FormationProvider>
  </QueryClientProvider>
);
