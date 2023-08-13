import ChatService from '@/setup/services/ChatService'
import Message from './Message'
import { Input } from '@/common/components/ui/input'
import { AiOutlineSend } from 'react-icons/ai'
import { useQuery } from 'react-query'
import { useEffect, useState } from 'react'
import { TMessage } from '@/common/types/message'
import useChatStore from '@/setup/stores/chat.store'
import { Button } from '@/common/components/ui/button'
import SocketService from '@/setup/services/SocketService'
import useAuthStore from '@/setup/stores/auth.store'

interface IProps {
  conversationId: string
}

function Conversation ( { conversationId }: IProps ): JSX.Element {
  const [ messages, setMessages ] = useState<TMessage[]>( [] )
  const [ message, setMessage ] = useState<string>( '' )
  const [ typing, setTyping ] = useState<boolean>( false )

  const { findConversation, isMyChat } = useChatStore()
  const { user } = useAuthStore()

  const query = useQuery( [ 'chatId', conversationId ], () => ChatService.fetchMessages( conversationId ), {
    onSuccess: () => {
      if ( query.data ) {
        setMessages( query.data )

      }
    }
  } )

  const takeover = () => {
    const conversation = findConversation( conversationId )

    if ( !conversation || !user ) return

    const { members } = conversation

    members.employee = user._id

    SocketService.emitTakeover( conversationId, members )
  }

  const finishConversation = () => {
    const conversation = findConversation( conversationId )

    if ( !conversation || !user ) return

    conversation.status = 'done'

    SocketService.emitFinishConversation( conversation )
  }

  const sendMessage = () => {
    if ( !user ) return

    const messageToSend = {
      text: message,
      sender: user,
      conversation: conversationId,
      createdAt: new Date()
    } satisfies TMessage



    SocketService.emitMessage( messageToSend )

  }

  useEffect( () => {
    SocketService.onIsTyping( ( data ) => {
      setTyping( data )
    } )
    SocketService.onRetrieveMessage( ( message ) => {
      setMessages( prev => [ ...prev, message ] )
    } )
  }, [] )

  return (
    <div className='relative p-8 h-[calc(100vh-60px)] w-full' >
      <div className='absolute top-2 right-2 flex flex-col'>
        {isMyChat( conversationId ) ? <Button variant='link' className='text-stone-400' onClick={takeover}>Takeover</Button> : null}
        <Button variant='link' className='text-green-600 underline' onClick={finishConversation}>Mark as done</Button>
      </div>

      <div className='h-[calc(100vh-150px)] overflow-y-auto overflow-x-hidden space-y-8 w-full' >
        {messages.map( message => <Message message={message} /> )}
        {typing ? <p>typing..</p> : null}
      </div>
      <div className='flex items-center gap-4 px-8'>
        <Input placeholder='message..' onChange={e => setMessage( e.target.value )} />
        <AiOutlineSend className="text-3xl cursor-pointer text-dark" onClick={sendMessage} />
      </div>
    </div>
  )
}

export default Conversation