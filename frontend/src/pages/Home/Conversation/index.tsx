import Message from './Message'
import { Input } from '@/common/components/ui/input'
import { AiOutlineSend } from 'react-icons/ai'

const message = {
  text: 'Hello world!',
  sender: {
    _id: '213',
    name: 'John Doe',
    email: 'joihnDoe@hotmail.com'
  },
  createdAt: new Date(),
  conversation: '123'
}


function Conversation () {

  return (
    <div className='p-8 h-[calc(100vh-60px)] w-full' >
      <div className='h-[calc(100vh-150px)] overflow-y-auto space-y-8 w-full' >
        <Message message={message} />
        <Message message={message} />
      </div>
      <div className='flex items-center gap-4 px-8'>
        <Input placeholder='message..' />
        <AiOutlineSend className="text-3xl cursor-pointer text-dark" />
      </div>
    </div>
  )
}

export default Conversation