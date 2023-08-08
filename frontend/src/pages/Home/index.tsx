import withNavbarProvided from '@/common/components/layout/Navbar'
import Inbox from './Inbox'
import Conversation from './Conversation'
import { useEffect } from 'react'
import SocketService from '@/setup/services/SocketService'
import { useQuery } from 'react-query'
import ChatService from '@/setup/services/ChatService'

function Home () {
  const queryConversations = useQuery( 'conversations', ChatService.fetchConversations, {
    onSuccess: () => {
      console.log( queryConversations.data )
    },
  } )

  useEffect( () => {
    SocketService.connect()

    return () => {
      SocketService.disconnect()
    }
  }, [] )

  return (
    <div className='flex'>
      <Inbox />
      <Conversation />
    </div>
  )
}

export default withNavbarProvided( Home )