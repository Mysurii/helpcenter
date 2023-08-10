import { TConversation } from '@/common/types/conversation';
import { create } from 'zustand'

interface ChatState {
  activeChatId: string | null;
  myInbox: TConversation[]
  openInbox: TConversation[]
  setActiveChat: ( chatId: string ) => void
  setConversations: ( userId: string, conversations: TConversation[] ) => void

}

const useChatStore = create<ChatState>()( ( set ) => ( {
  activeChatId: null,
  myInbox: [],
  openInbox: [],
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
      set( ( state ) => ( { ...state, myInbox, openInbox } ) )
    } )
  }
} ) )

export default useChatStore