import { TConversation } from '@/common/types/conversation';
import { TMessage } from '@/common/types/message';
import { create } from 'zustand'


interface ChatState {
  activeChatId: string | null;
  myInbox: TConversation[]
  openInbox: TConversation[]
  messages: TMessage[]
  setActiveChat: ( chatId: string ) => void


}

const useChatStore = create<ChatState>()( ( set ) => ( {
  activeChatId: null,
  myInbox: [],
  openInbox: [],
  messages: [],
  setActiveChat: ( chatId: string ) => set( ( state ) => ( { ...state, activeChatId: chatId } ) )
} ) )

export default useChatStore