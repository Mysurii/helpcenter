import { Link } from 'react-router-dom'

function Navbar () {
  return (
    <div className='flex justify-between items-center py-4 px-2 md:px-16 bg-dark text-white'>
      <h1 className='text-xl'>Vormats</h1>
      <div className='flex gap-4'>
        <Link to="/dashboard">dashboard</Link>
        <Link to="/signin">logout</Link>
      </div>
    </div>
  )
}


const withNavbarProvided = ( Component: React.ComponentType ) => {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  return function NavbarProvided ( props: any ): JSX.Element {
    return (
      <>
        <Navbar />
        <Component {...props} />
      </>
    )
  }
}

export default withNavbarProvided