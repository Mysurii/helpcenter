import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/common/components/ui/tabs"
import { Card } from "./Card"

const message = {
  text: 'Hello world!',
  sender: {
    _id: '213',
    name: 'John Doe',
    email: 'joihnDoe@hotmail.com'
  },
  createdAt: new Date(),
  conversationId: '123'
}

function Inbox () {
  return (
    <div className="w-full lg:w-1/6 h-[calc(100vh-60px)] border-r border-stone-200 overflow-y-auto">
      <Tabs defaultValue="inbox">
        <TabsList className="grid w-full grid-cols-2 bg-dark text-white">
          <TabsTrigger value="inbox">Open</TabsTrigger>
          <TabsTrigger value="my messages" >My Inbox</TabsTrigger>
        </TabsList>
        <TabsContent value="inbox">
          hello world!
        </TabsContent>
        <TabsContent value="my messages">
          <Card message={message} />
          <Card message={message} />
          <Card message={message} />
          <Card message={message} />
          <Card message={message} />
        </TabsContent>
      </Tabs>

    </div>
  )
}

export default Inbox