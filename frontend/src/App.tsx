import { BrowserRouter as Router } from 'react-router-dom'
import RouteRenderer from '@/setup/routes/routes-config'

function App () {

  return (
    <Router>
      <div className='bg-red-500'>
        Hello world!
      </div>
      <RouteRenderer />
    </Router>
  )
}

export default App
