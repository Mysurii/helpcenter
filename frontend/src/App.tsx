import { BrowserRouter as Router } from 'react-router-dom'
import RouteRenderer from '@/setup/routes/routes-config'

function App () {

  return (
    <Router>
      <RouteRenderer />
    </Router>
  )
}

export default App
