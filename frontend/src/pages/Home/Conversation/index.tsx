import ChatService from '@/setup/services/ChatService'
import Message from './Message'
import { Input } from '@/common/components/ui/input'
import { AiOutlineSend } from 'react-icons/ai'
import { useQuery } from 'react-query'
import { useState } from 'react'
import { TMessage } from '@/common/types/message'

interface IProps {
  conversationId: string
}

function Conversation ( { conversationId }: IProps ): JSX.Element {
  const [ messages, setMessages ] = useState<TMessage[]>( [] )
  console.log( conversationId )

  const query = useQuery( [ 'chatId', conversationId ], () => ChatService.fetchMessages( conversationId ), {
    onSuccess: () => {
      if ( query.data ) {
        setMessages( query.data )

      }
    }
  } )

  return (
    <div className='p-8 h-[calc(100vh-60px)] w-full ' >
      <div className='h-[calc(100vh-150px)] overflow-y-auto overflow-x-hidden space-y-8 w-full' >
        {messages.map( message => <Message message={message} /> )}
      </div>
      <div className='flex items-center gap-4 px-8'>
        <Input placeholder='message..' />
        <AiOutlineSend className="text-3xl cursor-pointer text-dark" />
      </div>
    </div>
  )
}

export default Conversation