import { BrowserRouter as Router } from 'react-router-dom'
import RouteRenderer from '@/setup/routes/routes-config'
import { QueryClient, QueryClientProvider } from 'react-query'
import { Toaster } from 'react-hot-toast'

const client = new QueryClient( {
  defaultOptions: {
    queries: {
      cacheTime: 1000 * 60 * 60 * 24, // 24 hours
    },
  },
} )

function App () {

  return (
    <QueryClientProvider client={client}>
      <Toaster />
      <Router>
        <RouteRenderer />
      </Router>
    </QueryClientProvider>
  )
}

export default App
