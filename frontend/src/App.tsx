import { SetStateAction, useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

import { Textarea } from "@/components/ui/textarea"
import { Button } from "@/components/ui/button"
import { ScrollArea } from "@/components/ui/scroll-area"
import { Separator } from "@/components/ui/separator"









function App() {
  const [count, setCount] = useState(0)

  const [domains, setDomains] = useState<Set<string>>(new Set())
  const [domainInput, setDomainInput] = useState("")

  const [keywords, setKeywords] = useState<Set<string>>(new Set())
  const [keywordInput, setKeywordInput] = useState("")

  let addDomain = () => {
    let helper = domainInput.split("\n")
    const newDomains = new Set(domains);

    helper.map((domain) => {
      console.log(domain);
      if (domain.trim() && !newDomains.has(domain)) {
        console.log("adding: ", domain);
        newDomains.add(domain); // Add to the new set
      }
    })
    setDomains(newDomains)
    setDomainInput("")
  }

  let addKeyword = () => {
    let helper = keywordInput.split("\n")
    const newKeywords = new Set(keywords);

    helper.map((keyword) => {
      console.log(keyword);
      if (keyword.trim() && !newKeywords.has(keyword)) {
        console.log("adding: ", keyword);
        newKeywords.add(keyword); // Add to the new set
      }
    })
    setKeywords(newKeywords)
    setKeywordInput("")
  }


  let handleTextChange = (event: { target: { name: string; value: SetStateAction<string> } }) => {
    if (event.target.name == "domainsTextArea") {
      setDomainInput(event.target.value)
    }
    else if (event.target.name == "keywordsTextArea") {
      setKeywordInput(event.target.value)
    }
  }

  let removeItem = (event: React.MouseEvent<HTMLButtonElement>) => {
    console.log('0000000000000')
    if (event.currentTarget.dataset.type == "domain") {
      let helper = new Set(domains)
      helper.delete(event.currentTarget.name)
      setDomains(helper)
    }
    else if (event.currentTarget.dataset.type == "keyword") {
      let helper = new Set(keywords)
      helper.delete(event.currentTarget.name)
      setKeywords(helper)
    }
  }


  return (
    <>
      <div className='flex flex-col w-[100vw] items-center'>
        <h1 className=''>DNS Manager</h1>
        <div className='flex flex-row gap-4 p-2'>
          <div className='flex flex-col gap-2'>
            <Textarea name='domainsTextArea' placeholder='enter domains here' value={domainInput} onChange={handleTextChange} />
            <Button name='removeDomain' variant="outline" onClick={addDomain}>Save</Button>
            <ScrollArea className="h-[200px] w-[350px] rounded-md border p-2 ">
              <div className='flex flex-col gap-1'>
                {Array.from(domains).map((domain) => (
                  <div>
                    <div className=' flex flex-row justify-between text-whit text-center'>
                      <div className='mt-1 ml-2'>{domain}</div>
                      <Button data-type='domain' name={`${domain}`} className='bg-transparent text-black hover:text-red-500 hover:bg-transparent border-[0px] shadow-none' onClick={removeItem}>x</Button>
                    </div>
                    <Separator />
                  </div>
                ))}

              </div>
            </ScrollArea>

          </div>
          <div className='flex flex-col gap-2'>
            <Textarea name='keywordsTextArea' placeholder='enter domains here' value={keywordInput} onChange={handleTextChange} />
            <Button variant="outline" onClick={addKeyword}>Save</Button>
            <ScrollArea className="h-[200px] w-[350px] rounded-md border p-2 ">
              <div className='flex flex-col gap-1'>
                {Array.from(keywords).map((keyword) => (
                  <div>
                    <div className=' flex flex-row justify-between text-whit text-center'>
                      <div className='mt-1 ml-2'>{keyword}</div>
                      <Button data-type='keyword' name={`${keyword}`} className='bg-transparent text-black hover:text-red-500 hover:bg-transparent border-[0px] shadow-none' onClick={removeItem}>x</Button>
                    </div>
                    <Separator />
                  </div>
                ))}

              </div>
            </ScrollArea>

          </div>
        </div>
      </div>
    </>
  )
}

export default App
