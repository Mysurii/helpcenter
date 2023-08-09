import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/common/components/ui/tabs"
import { Card } from "./Card"
import { useQuery } from "react-query"
import useAuthStore from "@/setup/stores/auth.store"
import useChatStore from "@/setup/stores/chat.store"
import ChatService from "@/setup/services/ChatService"
import { toast } from "react-hot-toast"
import { Skeleton } from "@/common/components/ui/skeleton"

const message = {
  text: 'Hello world!',
  sender: {
    _id: '213',
    name: 'John Doe',
    email: 'joihnDoe@hotmail.com'
  },
  createdAt: new Date(),
  conversation: '123'
}

function Inbox () {

  const { setConversations, myInbox, openInbox } = useChatStore()
  const { user } = useAuthStore()

  const { isLoading } = useQuery( 'conversations', ChatService.fetchConversations, {
    onSuccess: ( data ) => {
      console.log( data )
      console.log( user )
      if ( user ) {
        setConversations( user._id, data )
      }
    }, onError: () => {
      toast.error( 'Could not fetch the conversations.' )
    }
  } )


  return (
    <div className="w-full lg:w-1/6 h-[calc(100vh-60px)] border-r border-stone-200 overflow-y-auto">
      <Tabs defaultValue="inbox">
        <TabsList className="grid w-full grid-cols-2 bg-dark text-white">
          <TabsTrigger value="inbox">Open</TabsTrigger>
          <TabsTrigger value="my messages" >My Inbox</TabsTrigger>
        </TabsList>
        <TabsContent value="inbox">
          {isLoading ? <>
            <div className="flex items-center space-x-4">
              <Skeleton className="h-12 w-12 rounded-full" />
              <div className="space-y-2">
                <Skeleton className="h-4 w-[250px]" />
                <Skeleton className="h-4 w-[200px]" />
              </div>
            </div>
          </> :
            <>
              {openInbox.map( conversation => <Card message={conversation.lastMessage} /> )}
            </>
          }
        </TabsContent>
        <TabsContent value="my messages">
          {isLoading ? <>
            <div className="flex items-center space-x-4">
              <Skeleton className="h-12 w-12 rounded-full" />
              <div className="space-y-2">
                <Skeleton className="h-4 w-[250px]" />
                <Skeleton className="h-4 w-[200px]" />
              </div>
            </div>
          </> :
            <>
              {myInbox.map( conversation => <Card message={conversation.lastMessage} /> )}
            </>
          }
        </TabsContent>
      </Tabs>

    </div>
  )
}

export default Inbox