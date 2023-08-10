import withNavbarProvided from '@/common/components/layout/Navbar'
import Inbox from './Inbox'
import Conversation from './Conversation'
import { useEffect } from 'react'
import SocketService from '@/setup/services/SocketService'
import { useQuery } from 'react-query'
import ChatService from '@/setup/services/ChatService'
import useChatStore from '@/setup/stores/chat.store'

function Home () {
  const { activeChatId } = useChatStore()

  const queryConversations = useQuery( 'conversations', ChatService.fetchConversations, {
    onSuccess: () => {
      console.log( queryConversations.data )
    },
  } )

  console.log( activeChatId )

  useEffect( () => {
    SocketService.connect()

    return () => {
      SocketService.disconnect()
    }
  }, [] )

  return (
    <div className='flex'>
      <Inbox />
      {activeChatId ? <Conversation conversationId={activeChatId} /> : <div className='grid place-items-center h-[calc(100vh-60px)] w-full' >
        <div className='text-center'>
          <h1 className='text-2xl text-primary font-bold'>No conversastion selected</h1>
          <p className='font-semibold text-stone-500'>Select a chat from your inbox</p>
        </div>

      </div>}

    </div>
  )
}

export default withNavbarProvided( Home )