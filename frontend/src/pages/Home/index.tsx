import withNavbarProvided from '@/common/components/layout/Navbar'
import Inbox from './Inbox'
import Conversation from './Conversation'

function Home () {
  return (
    <div className='flex'>
      <Inbox />
      <Conversation />
    </div>
  )
}

export default withNavbarProvided( Home )