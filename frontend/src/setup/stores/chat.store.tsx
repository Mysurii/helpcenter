import { TConversation } from '@/common/types/conversation';
import { create } from 'zustand'

interface ChatState {
  activeChatId: string | null;
  myInbox: TConversation[]
  openInbox: TConversation[]
  addConversation: ( conversation: TConversation ) => void
  findConversation: ( id: string ) => TConversation | undefined
  isMyChat: ( id: string ) => boolean,
  setActiveChat: ( chatId: string ) => void
  setConversations: ( userId: string, conversations: TConversation[] ) => void,
  removeConversation: ( userId: string, chatId: string ) => void
  updateConversation: ( userId: string, conversation: TConversation ) => void
}

const useChatStore = create<ChatState>()( ( set, get ) => ( {
  activeChatId: null,
  myInbox: [],
  openInbox: [],
  findConversation: ( id: string ) => {
    const conversations = [ ...get().myInbox, ...get().openInbox ]
    return conversations.find( x => x._id === id )
  },
  isMyChat: ( id: string ) => get().myInbox.map( x => x._id ).includes( id ),
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
  },
  addConversation: ( conversation: TConversation ) => set( ( state ) => ( { ...state, openInbox: [ ...get().openInbox, conversation ] } ) ),
  removeConversation: ( userId: string, chatId: string ) => {
    let conversations = [ ...get().myInbox, ...get().openInbox ]

    conversations = conversations.filter( x => x._id !== chatId )

    get().setConversations( userId, conversations )
  },
  updateConversation: ( userId: string, conversation: TConversation ) => {
    const conversations = [ ...get().myInbox, ...get().openInbox ]
    const idx = conversations.map( x => x._id ).indexOf( conversation._id )

    if ( !idx ) return

    conversations[ idx ] = conversation
    get().setConversations( userId, conversations )

  }
} ) )

export default useChatStore