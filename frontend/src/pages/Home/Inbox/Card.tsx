import moment from 'moment'
import { getInitials } from "@/common/lib/utils"
import { TMessage } from "@/common/types/message"
import useChatStore from '@/setup/stores/chat.store'
import { BiDotsVerticalRounded } from 'react-icons/bi'

interface IProps {
  message: TMessage
  myConversation?: boolean
}


export function Card ( { message }: IProps ) {
  const { setActiveChat } = useChatStore()
  const { text, sender } = message
  const initials = getInitials( sender.name )
  const relativeTime = moment( message.createdAt ).fromNow()
  const trunctuated = text.length > 30 ? text.substring( 0, 30 ) + '...' : text

  const setActive = ( id: string ) => {
    setActiveChat( id )
  }

  return (
    <div className="relative flex items-center p-3 h-[75px] border-b border-stone-200 cursor-pointer hover:bg-slate-900 hover:text-white" onClick={() => setActive( message.conversation )}>
      <div className="relative inline-flex items-center justify-center w-10 h-10 overflow-hidden rounded-full bg-neutral-600">
        <span className="font-medium text-gray-300">{initials}</span>
      </div>
      <div className="pl-2 text-sm">
        <p>{trunctuated}</p>
        <span className='text-gray-500 text-xs mt-1'>{relativeTime}</span>
      </div>
      <BiDotsVerticalRounded className="absolute top-2 right-2 cursor-pointer" />
    </div>
  )
}