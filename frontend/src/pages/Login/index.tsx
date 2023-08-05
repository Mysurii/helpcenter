import { Button } from "@/common/components/ui/button"
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/common/components/ui/form"
import { Input } from "@/common/components/ui/input"
import { TLoginSchema, loginSchema } from "@/common/validations/login.validation"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import loginSvg from '@/assets/svg/login.svg'
import { RxAvatar } from 'react-icons/rx'

function Login () {
  const form = useForm<TLoginSchema>( {
    resolver: zodResolver( loginSchema ),
    defaultValues: {
      email: "",
      password: ""
    },
  } )

  const onSubmit = ( values: TLoginSchema ) => {
    console.log( values )
  }

  return (
    <div className="flex h-screen justify-center bg-dark text-white lg:bg-white lg:text-black">
      <div className="flex flex-col justify-center items-center p-8 w-full lg:w-1/3">
        <RxAvatar className="text-5xl text-violet-600" />
        <p className="text-red-500 font-semibold my-3">Invalid credentials</p>
        <Form {...form}>
          <form onSubmit={form.handleSubmit( onSubmit )} className="space-y-4 w-full max-w-lg">
            <FormField
              control={form.control}
              name="email"
              render={( { field } ) => (
                <FormItem>
                  <FormLabel>E-mail</FormLabel>
                  <FormControl>
                    <Input placeholder="email" {...field} />
                  </FormControl>

                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="password"
              render={( { field } ) => (
                <FormItem>
                  <FormLabel>Password</FormLabel>
                  <FormControl>
                    <Input placeholder="password" type="password"  {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <Button variant="outline" type="submit" size="sm" className="text-violet-700 rounded-3xl px-8">Sign In</Button>
          </form>
        </Form>
      </div>

      <div className="hidden lg:flex justify-center items-center w-2/3 bg-dark">
        <img src={loginSvg} alt="image" className="rounded max-w-2xl object-cover" />
      </div>

    </div>
  )
}

export default Login
