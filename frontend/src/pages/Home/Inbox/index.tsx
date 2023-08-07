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
      <Card message={message} />
      <Card message={message} />
      <Card message={message} />
      <Card message={message} />
      <Card message={message} />
      <Card message={message} />
      <Card message={message} />
      <Card message={message} />
      <Card message={message} />
      <Card message={message} />
      <Card message={message} />
      <Card message={message} />
    </div>
  )
}

export default Inbox