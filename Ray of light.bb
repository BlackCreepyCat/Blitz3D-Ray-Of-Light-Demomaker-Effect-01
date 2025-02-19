;3D-RAY-DEMO

Graphics3D (640,480,32,2)
SeedRnd MilliSecs()

Camera = CreateCamera()

fnt = LoadFont("Terminal",25,25)
SetFont fnt

MaxPlanes = 100

Dim Sprite( MaxPlanes+2 )

tex1 = CreateTexture(256 , 256)      ;Textur Hintergrund
tex2 = CreateTexture(256 , 256 , 3)  ;Textur Sprites

;Sprites erstellen und Textur zuweisen
For t = 0 To MaxPlanes
  Sprite(t) = CreateSprite()
  SpriteViewMode Sprite(t) , 2
  EntityTexture Sprite(t) , tex2
  ScaleSprite Sprite(t) , 15 , 15
  PositionEntity Sprite(t),0,0,t*0.08
Next
EntityTexture Sprite(MaxPlanes) , tex1

;Zufalls-Schrift erzeugen
SetBuffer TextureBuffer(tex1)
For tt = -4 To 4
  b$ = ""
  For t = 1 To Rnd(15 , 20)
    b$ = b$ + Chr$(Rnd(32 , 128))
  Next
  Color Rnd(50,255) , Rnd(50,255) , Rnd(50,255)
  Text 128 , 128 + tt * 20 , b$ , 1 , 1
Next

;Textur verwischen
blur 0 , 0 , 256 , 256 , 0 , 0 , TextureBuffer(tex1) , TextureBuffer(tex2) , 0.25 , 5

Function blur(x1% , y1% , xs% , ys% , x2% , y2% , bf1 , bf2 , m# , p%)
  p = p Shl 56
  LockBuffer bf1
  LockBuffer bf2
  For y% = y1 + 1 To y1 + ys - 1
  For x% = x1 + 1 To x1 + xs - 1
    col1 = ReadPixelFast(x - 1 , y , bf1)
    col2 = ReadPixelFast(x + 1 , y , bf1)
    col3 = ReadPixelFast(x , y - 1 , bf1)
    col4 = ReadPixelFast(x , y + 1 , bf1)
    col1r%=(col1 Shr 16) And 255: col1g%=(col1 Shr 8) And 255: col1b%=(col1) And 255
    col2r%=(col2 Shr 16) And 255: col2g%=(col2 Shr 8) And 255: col2b%=(col2) And 255
    col3r%=(col3 Shr 16) And 255: col3g%=(col3 Shr 8) And 255: col3b%=(col3) And 255
    col4r%=(col4 Shr 16) And 255: col4g%=(col4 Shr 8) And 255: col4b%=(col4) And 255
    colr% = (col1r + col2r + col3r + col4r) * m
    colg% = (col1g + col2g + col3g + col4g) * m
    colb% = (col1b + col2b + col3b + col4b) * m
    WritePixelFast x + x2 , y + y2 , p + (colr Shl 16 + colg Shl 8 + colb) , bf2
  Next
  Next
  UnlockBuffer bf1
  UnlockBuffer bf2
End Function


;und los gehts
SetBuffer BackBuffer() 
While Not KeyHit(1)
  mm = (mm + 1) Mod 360
  mn# = (mn# + 0.35) Mod 360

  mx# = Sin(mm) * 8
  my# = Cos(mm) * 5
  mz# = -5 + Cos(mn) * 15

  PositionEntity Camera , mx , my , 0
  RotateEntity Camera , 0 , 0 , mz
  RenderWorld
  Flip
Wend
End 
;~IDEal Editor Parameters:
;~C#Blitz3D