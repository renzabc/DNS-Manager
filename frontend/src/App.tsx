import { SetStateAction, useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

import { Textarea } from "@/components/ui/textarea"
import { Button } from "@/components/ui/button"
import { ScrollArea } from "@/components/ui/scroll-area"









function App() {
  const [count, setCount] = useState(0)

  const [domains, setDomains] = useState<Set<string>>(new Set())
  const [domainInput, setDomainInput] = useState("")

  let addDomain = () => {
    
    // if (domainInput.trim() && !domains.has(domainInput)) {
    //   setDomains(new Set(domains).add(domainInput))
    //   setDomainInput("")
    // }
  }

  let handleTextChange = (event: { target: { value: SetStateAction<string> } }) => {
    setDomainInput(event.target.value)
  }


  return (
    <>
      <div className=''>
        <Textarea placeholder='enter domains here' value={domainInput} onChange={handleTextChange} />
        <Button variant="outline" onClick={addDomain}>Button</Button>
        <ScrollArea className="h-[200px] w-[350px] rounded-md border p-4">
          {Array.from(domains).map((domain) => (
            <Button>{domain}</Button>
          ))}
        </ScrollArea>

      </div>
    </>
  )
}

export default App
