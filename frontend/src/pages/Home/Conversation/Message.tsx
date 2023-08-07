import { cn } from "@/common/lib/utils"
import { TMessage } from "@/common/types/message"
import moment from "moment"

interface IProps {
  message: TMessage
}

function Message ( { message }: IProps ) {
  const relativeTime = moment( message.createdAt ).fromNow()
  const isSelfMessage = message.sender._id == '2313'
  console.log( isSelfMessage )

  return (
    <div className={cn( "flex flex-col", [ isSelfMessage && 'items-end' ] )}>
      <div className={cn( "relative  text-white p-3 rounded-xl max-w-xs bg-neutral-400", [ isSelfMessage && 'bg-dark' ] )}>
        Hey there! How can I help you?
        <div className={cn( "absolute w-3 h-3 transform rotate-45 top-5", [
          isSelfMessage ? '-right-1 bg-dark' : '-left-1 bg-neutral-400'
        ] )}></div>
      </div>
      <div className="text-gray-500 text-xs mt-1 ml-8">
        {relativeTime}
      </div>
    </div>
  )
}

export default Message