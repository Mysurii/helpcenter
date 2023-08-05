import notFound from '@/assets/svg/notFound.svg'
import { Button } from '@/common/components/ui/button'
import { Link } from 'react-router-dom'
import { BiSolidChevronLeft } from 'react-icons/bi'

function NotFound () {
  return (
    <div className="flex justify-center flex-col items-center mt-[20vh]">
      <h1 className="text-3xl primary font-bold">404 Not found</h1>
      <p className='text-lg primary'>Looks like this page is missing. Please go back to the homepage.</p>
      <img src={notFound} alt="not found" className='w-[20vw] my-[10vh]' />
      <Link to="/">
        <Button variant={'link'}><BiSolidChevronLeft className="text-lg" /> Go to homepage</Button>
      </Link>

    </div>
  )
}

export default NotFound