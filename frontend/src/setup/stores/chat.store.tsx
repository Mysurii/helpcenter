import { TConversation } from '@/common/types/conversation';
import { TMessage } from '@/common/types/message';
import { create } from 'zustand'

interface ChatState {
  activeChatId: string | null;
  myInbox: TConversation[]
  openInbox: TConversation[]
  messages: TMessage[]
  setActiveChat: ( chatId: string ) => void
  setConversations: ( userId: string, conversations: TConversation[] ) => void

}

const useChatStore = create<ChatState>()( ( set ) => ( {
  activeChatId: null,
  myInbox: [],
  openInbox: [],
  messages: [],
  setActiveChat: ( chatId: string ) => set( ( state ) => ( { ...state, activeChatId: chatId } ) ),
  setConversations: ( userId: string, conversations: TConversation[] ) => {
    const myInbox: TConversation[] = []
    const openInbox: TConversation[] = []

    conversations.forEach( conversation => {
      if ( conversation.members.employee === userId ) {
        myInbox.push( conversation )
      } else {
        openInbox.push( conversation )
      }
      set( { myInbox, openInbox } )
    } )
  }
} ) )

export default useChatStore